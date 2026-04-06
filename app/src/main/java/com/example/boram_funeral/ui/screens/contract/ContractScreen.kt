package com.example.boram_funeral.ui.screens.contract

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.contracts.funeral.*
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import androidx.compose.runtime.CompositionLocalProvider
import com.example.boram_funeral.ui.screens.contract.pdf.ContractPdfExporter
import com.example.boram_funeral.ui.screens.contract.pdf.ContractScrollRegistry
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import com.example.boram_funeral.ui.screens.contract.pdf.PdfCaptureHelper
import com.example.funeralcontract.ui.FuneralContractStep

// ── Step 뷰 캡처를 위한 key 관리 ──────────────────────────────────────────────

/**
 * 각 페이지 Composable의 View 참조를 수집하기 위한 상태
 * PDF 저장 시 각 Step을 비트맵으로 캡처하는 데 사용
 */

@Composable
fun ContractScreen(
    onDismiss: () -> Unit,
    contractViewModel: ContractViewModel,
) {
    val context = LocalContext.current
    val view    = LocalView.current
    val scope   = rememberCoroutineScope()

    // ── Step 리스트 ───────────────────────────────────────────────────────────
    val contractSteps = listOf<@Composable () -> Unit>(
        { ReceptionBasicStep(viewModel = contractViewModel) },
        { DeceasedDetailStep(viewModel = contractViewModel) },
        { CasketShroudStep(viewModel = contractViewModel) },
        { FoodCateringStep(viewModel = contractViewModel) },
        { FuneralContractStep(viewModel = contractViewModel) },
        { CeremonyOrderStep() },
        { FuneraltermsStep(viewModel = contractViewModel) },
        { PrivacyConsentStep(viewModel = contractViewModel) },
        { DeceasedInfoConsentStep(viewModel = contractViewModel) },
        { CustomerConfirmStep(viewModel = contractViewModel) },
        { FamilyInfoStep(viewModel = contractViewModel) },
    )

    val pagerState = rememberPagerState(pageCount = { contractSteps.size })
    val isLastPage by remember {
        derivedStateOf { pagerState.currentPage == contractSteps.size - 1 }
    }

    // PDF 내보내기 진행 상태
    var isExporting by remember { mutableStateOf(false) }

    // 각 Step의 ScrollState 레지스트리 — PDF 캡처 시 programmatic scroll에 사용
    val scrollRegistry = remember { ContractScrollRegistry() }

    // 콘텐츠 영역(페이저) 위치 — 닫기 버튼·하단 바 제외
    var contentTopPx    by remember { mutableIntStateOf(0) }
    var contentHeightPx by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
        }

        CompositionLocalProvider(
            LocalScrollStateRegistrar provides { page, state -> scrollRegistry.register(page, state) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned { coords ->
                        contentTopPx    = coords.positionInRoot().y.toInt()
                        contentHeightPx = coords.size.height
                    }
            ) {
                UseContractStepContent(
                    pagerState = pagerState,
                    contractSteps = contractSteps,
                    onClose = onDismiss,
                    onFinish = onDismiss,
                    showHeader = !isExporting,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
        ) {
            StepBottomBar(
                currentPage = pagerState.currentPage,
                isLastPage = isLastPage,
                isExporting = isExporting,
                onPrev = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                onNext = {
                    if (!isLastPage) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        // 마지막 페이지 → PDF 저장
                        contractViewModel.saveData {
                            scope.launch {
                                isExporting = true
                                exportContractPdf(
                                    context = context,
                                    rootView = view,
                                    pagerState = pagerState,
                                    totalSteps = contractSteps.size,
                                    deceasedName = contractViewModel.uiState.value.deceasedName,
                                    contentTopPx = contentTopPx,
                                    contentHeightPx = contentHeightPx,
                                    scrollRegistry = scrollRegistry,
                                    onExported = { intent ->
                                        isExporting = false
                                        contractViewModel.clearData()
                                        if (intent != null) {
                                            context.startActivity(
                                                Intent.createChooser(intent, "계약서 저장")
                                            )
                                        }
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }

}

// ── PDF 내보내기 — Pager를 순회하며 각 페이지 캡처 ───────────────────────────

/**
 * Pager를 각 Step으로 이동시키면서 현재 화면을 비트맵으로 캡처,
 * 모든 Step 캡처 완료 후 PDF로 합성합니다.
 *
 * [captureStepBitmap]은 현재 보이는 Pager 페이지를 캡처하는 확장 함수입니다.
 * 실제 View 캡처는 [PdfCaptureHelper]를 통해 수행됩니다.
 */
private suspend fun exportContractPdf(
    context: android.content.Context,
    rootView: android.view.View,
    pagerState: PagerState,
    totalSteps: Int,
    deceasedName: String = "",
    contentTopPx: Int = 0,
    contentHeightPx: Int = 0,
    scrollRegistry: ContractScrollRegistry,
    onExported: (Intent?) -> Unit
) {
    val bitmaps = mutableListOf<android.graphics.Bitmap>()
    val originalPage = pagerState.currentPage

    try {
        // 각 Step으로 이동 후 전체 내용 캡처
        for (i in 0 until totalSteps) {
            pagerState.scrollToPage(i)
            kotlinx.coroutines.delay(400)

            val resolvedContentHeight = if (contentHeightPx > 0) contentHeightPx else rootView.height
            val scrollState = scrollRegistry[i]
            val bitmap = if (scrollState != null) {
                // programmatic scroll — 전체 계약서 내용을 정확하게 캡처
                PdfCaptureHelper.captureWithScrollState(
                    scrollState     = scrollState,
                    view            = rootView,
                    contentTopPx    = contentTopPx,
                    contentHeightPx = resolvedContentHeight,
                )
            } else {
                // 폴백: 드래그 시뮬레이션 방식
                PdfCaptureHelper.captureFullScrollContent(
                    view            = rootView,
                    contentTopPx    = contentTopPx,
                    contentHeightPx = resolvedContentHeight,
                )
            }
            bitmaps.add(bitmap)
        }

        // 원래 페이지로 복원
        pagerState.scrollToPage(originalPage)

        // PDF 생성
        val pdfDocument = android.graphics.pdf.PdfDocument()
        // 페이지 너비를 A4 폭(595pt)으로 고정하고, 높이는 비트맵 비율에 맞게 계산
        val pageWidth = 595
        bitmaps.forEachIndexed { index, bitmap ->
            val scale = pageWidth.toFloat() / bitmap.width
            val pageHeight = (bitmap.height * scale).toInt().coerceAtLeast(1)
            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(
                pageWidth, pageHeight, index + 1
            ).create()
            val page = pdfDocument.startPage(pageInfo)
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                isFilterBitmap = true
            }
            val matrix = android.graphics.Matrix().apply { postScale(scale, scale) }
            page.canvas.drawBitmap(bitmap, matrix, paint)
            pdfDocument.finishPage(page)
            bitmap.recycle()
        }

        // PDF 먼저 캐시에 저장 (MediaStore 복사 원본으로 사용)
        val cacheDir = java.io.File(context.cacheDir, "pdf").also { it.mkdirs() }
        val dateStr = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
            .format(java.util.Date())
        val namePart = if (deceasedName.isNotBlank()) "_$deceasedName" else ""
        val fileName = "[장례식장계약서]_${dateStr}${namePart}.pdf"
        val cacheFile = java.io.File(cacheDir, fileName)
        java.io.FileOutputStream(cacheFile).use { pdfDocument.writeTo(it) }

        // ── 저장 경로 — 태블릿 파일 앱 Downloads 에서 바로 확인 가능 ──────────
        val outputFile = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Android 10 이상 — MediaStore로 Downloads 에 저장
            saveToDownloadsViaMediaStore(context, fileName)
        } else {
            // Android 9 이하 — 공용 Downloads 폴더 직접 접근
            val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(
                android.os.Environment.DIRECTORY_DOWNLOADS
            ).also { it.mkdirs() }
            java.io.File(downloadsDir, fileName).also { file ->
                pdfDocument.writeTo(java.io.FileOutputStream(file))
            }
        }

        pdfDocument.close()

        if (outputFile == null) {
            onExported(null)
            return
        }

        android.util.Log.d("ContractScreen", "PDF 저장 완료: ${outputFile.absolutePath}")

        // 공유 시트 — 저장 위치 확인 or 다른 앱으로 열기
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", outputFile
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "장례계약서")
            putExtra(Intent.EXTRA_TEXT, "저장 경로: Downloads/$fileName")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        onExported(intent)

    } catch (e: Exception) {
        android.util.Log.e("ContractScreen", "PDF 내보내기 실패", e)
        onExported(null)
    }
}

// ── Downloads 저장 헬퍼 (Android 10+) ────────────────────────────────────────

/**
 * Android 10(Q) 이상에서 MediaStore를 통해 공용 Downloads 폴더에 PDF를 저장합니다.
 * 저장 후 FileProvider로 공유하기 위해 캐시에도 복사본을 만들어 반환합니다.
 */
private fun saveToDownloadsViaMediaStore(
    context: android.content.Context,
    fileName: String
): java.io.File? {
    return try {
        val resolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = android.provider.MediaStore.Downloads.getContentUri(
            android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
        )
        val itemUri = resolver.insert(collection, contentValues) ?: return null

        // MediaStore에 쓰기
        resolver.openOutputStream(itemUri)?.use { outputStream ->
            // 캐시에서 읽어서 MediaStore에 복사
            val cacheDir = java.io.File(context.cacheDir, "pdf").also { it.mkdirs() }
            val cacheFile = java.io.File(cacheDir, fileName)
            cacheFile.inputStream().copyTo(outputStream)
        }

        contentValues.clear()
        contentValues.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(itemUri, contentValues, null, null)

        android.util.Log.d("ContractScreen", "MediaStore Downloads 저장 완료: $fileName")

        // FileProvider용 캐시 파일 반환
        java.io.File(java.io.File(context.cacheDir, "pdf"), fileName)

    } catch (e: Exception) {
        android.util.Log.e("ContractScreen", "MediaStore 저장 실패", e)
        null
    }
}

@Composable
fun StepBottomBar(
    currentPage: Int,
    isLastPage: Boolean,
    isExporting: Boolean = false,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Large,
                    text = "이전",
                    onClick = onPrev,
                    fullWidth = true,
                    enabled = currentPage > 0 && !isExporting,
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Large,
                    text = when {
                        isExporting -> "저장 중..."
                        isLastPage  -> "저장하기"
                        else        -> "다음"
                    },
                    onClick = onNext,
                    fullWidth = true,
                    enabled = !isExporting
                )
            }
        }
    }
}