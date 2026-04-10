package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.HandwrittenAreaCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel

// ── 색상 ──────────────────────────────────────────────────────────────────────
private val COLOR_BRAND  = Color(0xFF05195F)
private val COLOR_HEADER = Color(0xFFF5F5F5)
private val COLOR_BORDER = Color(0xFFD1D1D1)

// ── 테이블 행 데이터 ──────────────────────────────────────────────────────────

private data class ConfirmRow(
    val no: Int,
    val content: String,
    val reference: String,
)

private val confirmRows = listOf(
    ConfirmRow(1, "장례식장 영업자의 성명\n(법인 대표자 성명)", "장례식장 이용 계약서"),
    ConfirmRow(2, "장례의식의 내용", "상담사의 안내"),
    ConfirmRow(3, "장례식장 이용기간 및 이용시설", "1. 장례식장 이용계약서\n2. 이용약관 제4조 및 제5조"),
    ConfirmRow(4, "장례식장 이용료 및\n그 지급방법과 시기", "1. 장례식장 이용계약서\n2. 이용약관 제6조"),
    ConfirmRow(5, "장례식장 이용에 관한 약관", "-"),
)

// ── 계약자 확인 항목 ──────────────────────────────────────────────────────────

private val confirmCheckItems = listOf(
    "① 장례식장 영업자의 성명(법인 대표자 성명)",
    "② 장례의식의 내용",
    "③ 장례식장 이용기간 및 이용시설",
    "④ 장례식장 이용료 및 그 지급방법과 시기",
)

// ── 메인 Screen ───────────────────────────────────────────────────────────────

@Composable
fun CustomerConfirmStep(viewModel: ContractViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    var signatureData by remember { mutableStateOf<Path?>(null) }

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {

            // ── 제목 ──────────────────────────────────────────────────────────
            Text(
                text = "보람인천장례식장 이용계약 안내 및 고객 확인서",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = COLOR_BRAND,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // ── 1. 계약의 주요 내용 안내 ──────────────────────────────────────
            SectionTitle("1. 계약의 주요 내용 안내")

            Text(
                text = "아래의 내용은 보람상조개발(주) 보람인천장례식장의 장례행사서비스의 주요 내용만을 설명한 것으로, 상세 내용은 장례식장 이용계약서 및 약관을 참조하시기 바랍니다.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ── 계약 주요 내용 테이블 ─────────────────────────────────────────
            ConfirmTable(
                ticks = uiState.confirmExplanationTicks,
                onDraw = { idx -> viewModel.tickConfirmExplanation(idx) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── 2. 계약자 확인 ────────────────────────────────────────────────
            SectionTitle("2. 계약자 확인")

            // 확인 박스
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, COLOR_BORDER)
                    .padding(14.dp)
            ) {
                Text(
                    text = "본인은 보람상조개발(주)가 운영하는 보람인천장례식장에서 장례행사 서비스를 이용하기 위한 계약을 체결하기 전에,",
                    fontSize = 16.sp,
                    color = Color(0xFF222222),
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                confirmCheckItems.forEach { item ->
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        color = Color(0xFF222222),
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "위 사항들을 상담사로부터 충분히 설명을 듣고 계약을 체결하였으며,\n계약서 및 약관을 교부받았음을 확인합니다.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222),
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── 하단 날짜 및 서명 ─────────────────────────────────────────────
            CustomerConfirmFooter(
                year = remember { java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString() },
                updateTick = uiState.confirmSignatureUpdateTick,
                capturedPath = signatureData,
                onSignatureClick = { viewModel.showConfirmSignatureDialog() }
            )
        }
    }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    if (uiState.isConfirmSignatureDialogVisible) {
        SignatureDialog(
            onDismiss = { viewModel.dismissConfirmSignatureDialog() },
            onConfirm = { path ->
                signatureData = Path().apply { addPath(path) }
                viewModel.confirmConfirmSignature()
            }
        )
    }
}

@Composable
private fun CustomerConfirmFooter(
    year: String,
    onSignatureClick: () -> Unit,
    updateTick: Int,
    capturedPath: Path?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 날짜
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("${year}년", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(16.dp))
            HandwrittenDateUnit(width = 80.dp)
            Text("월", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
            HandwrittenDateUnit(width = 80.dp)
            Text("일", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 서명란
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "계약자(이용자)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 56.dp)
                        .background(COLOR_HEADER)
                        .border(0.5.dp, COLOR_BORDER)
                        .clickable { onSignatureClick() },
                    contentAlignment = Alignment.Center
                ) {
                    key(updateTick) {
                        if (capturedPath != null) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .clipToBounds()
                            ) {
                                val bounds = capturedPath.getBounds()
                                if (bounds.width > 0 && bounds.height > 0) {
                                    val scale = minOf(
                                        size.width / bounds.width,
                                        size.height / bounds.height
                                    ) * 0.8f
                                    withTransform({
                                        translate(center.x, center.y)
                                        scale(scale, scale, Offset.Zero)
                                        translate(-bounds.center.x, -bounds.center.y)
                                    }) {
                                        drawPath(
                                            path = capturedPath,
                                            color = Color.Black,
                                            style = Stroke(
                                                width = 3f,
                                                cap = StrokeCap.Round,
                                                join = StrokeJoin.Round
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("성명 (인)", fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// ── 계약 주요 내용 테이블 ─────────────────────────────────────────────────────

@Composable
private fun ConfirmTable(
    ticks: List<Int>,
    onDraw: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, COLOR_BORDER)
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(COLOR_HEADER)
                .height(IntrinsicSize.Min)
        ) {
            ConfirmHeaderCell("No",              0.6f)
            VLine()
            ConfirmHeaderCell("계약 주요 내용 안내", 3f)
            VLine()
            ConfirmHeaderCell("참 조",            2.5f)
            VLine()
            ConfirmHeaderCell("설명 여부",         2f)
        }

        // 데이터 행
        confirmRows.forEachIndexed { idx, row ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(COLOR_BORDER)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // No
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${row.no}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = COLOR_BRAND,
                        textAlign = TextAlign.Center
                    )
                }
                VLine()
                // 계약 주요 내용
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = row.content,
                        fontSize = 16.sp,
                        color = Color(0xFF222222),
                        lineHeight = 24.sp
                    )
                }
                VLine()
                // 참조
                Box(
                    modifier = Modifier
                        .weight(2.5f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = row.reference,
                        fontSize = 16.sp,
                        color = Color(0xFF555555),
                        lineHeight = 24.sp
                    )
                }
                VLine()
                // 설명 여부 — 펜 직접 입력
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(64.dp)
                ) {
                    HandwrittenAreaCell(
                        modifier = Modifier.fillMaxSize(),
                        externalTick = ticks.getOrElse(idx) { 0 },
                        onDraw = { onDraw(idx) },
                        hint = "설명 들었음",
                    )
                }
            }
        }
    }
}

// ── 유틸 Composable ───────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = COLOR_BRAND,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun RowScope.ConfirmHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = COLOR_BRAND,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .weight(weight)
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
private fun RowScope.VLine() {
    Box(
        modifier = Modifier
            .width(0.5.dp)
            .fillMaxHeight()
            .background(COLOR_BORDER)
    )
}
