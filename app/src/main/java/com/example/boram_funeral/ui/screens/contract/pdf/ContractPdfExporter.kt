package com.example.boram_funeral.ui.screens.contract.pdf

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * 각 Step Composable을 비트맵으로 렌더링한 뒤 PDF로 내보냅니다.
 *
 * 사용법:
 * ```
 * ContractPdfExporter.export(context, stepComposables) { uri ->
 *     // 공유 시트 열기
 * }
 * ```
 */
object ContractPdfExporter {

    private const val TAG = "ContractPdfExporter"

    // A4 @ 150dpi
    private const val PAGE_WIDTH_PX  = 1240
    private const val PAGE_HEIGHT_PX = 1754

    // PDF pt (72dpi 기준 A4)
    private const val PDF_PAGE_WIDTH  = 595
    private const val PDF_PAGE_HEIGHT = 842

    /**
     * [stepViews] : 이미 inflate/측정 완료된 View 목록 (각 Step 1개)
     * 각 View를 비트맵으로 캡처 → PDF 페이지로 합성 → 파일 저장 → 공유 Intent 반환
     */
    suspend fun exportFromViews(
        context: Context,
        stepViews: List<View>,
        fileName: String = "장례계약서_${System.currentTimeMillis()}.pdf"
    ): Intent? = withContext(Dispatchers.IO) {
        try {
            val pdfDocument = PdfDocument()

            stepViews.forEachIndexed { index, view ->
                val bitmap = captureToBitmap(view)
                val pageInfo = PdfDocument.PageInfo.Builder(
                    PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT, index + 1
                ).create()
                val page = pdfDocument.startPage(pageInfo)
                drawBitmapToCanvas(page.canvas, bitmap)
                pdfDocument.finishPage(page)
                bitmap.recycle()
            }

            val outputDir = File(context.cacheDir, "pdf").also { it.mkdirs() }
            val file = File(outputDir, fileName)
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            Log.d(TAG, "PDF 저장 완료: ${file.absolutePath}")

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (e: Exception) {
            Log.e(TAG, "PDF 내보내기 실패", e)
            null
        }
    }

    private fun captureToBitmap(view: View): Bitmap {
        val width  = view.width.takeIf  { it > 0 } ?: PAGE_WIDTH_PX
        val height = view.height.takeIf { it > 0 } ?: PAGE_HEIGHT_PX

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    private fun drawBitmapToCanvas(pdfCanvas: Canvas, bitmap: Bitmap) {
        val scaleX = PDF_PAGE_WIDTH.toFloat()  / bitmap.width
        val scaleY = PDF_PAGE_HEIGHT.toFloat() / bitmap.height
        val scale  = minOf(scaleX, scaleY)

        val paint = Paint().apply { isAntiAlias = true; isFilterBitmap = true }
        val matrix = android.graphics.Matrix().apply {
            postScale(scale, scale)
        }
        pdfCanvas.drawBitmap(bitmap, matrix, paint)
    }
}