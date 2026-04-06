package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.HandwrittenAreaCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import kotlin.String

// ── 색상 ──────────────────────────────────────────────────────────────────────

private val ColorPrimary   = Color(0xFF05195F)
private val ColorBg        = Color(0xFFF9F9F9)
private val ColorBorder    = Color(0xFF888888)
private val ColorTableHead = Color(0xFFEEF2FF)
private val ColorText      = Color(0xFF222222)
private val ColorSub       = Color(0xFF555555)
private val ColorConfirmBg = Color(0xFFFFFDE7)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {

            // ── 제목 ──────────────────────────────────────────────────────────
            Text(
                text = "보람인천장례식장 이용계약 안내 및 고객 확인서",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // ── 1. 계약의 주요 내용 안내 ──────────────────────────────────────
            SectionTitle("1. 계약의 주요 내용 안내")

            Text(
                text = "아래의 내용은 보람상조개발(주) 보람인천장례식장의 장례행사서비스의 주요 내용만을 설명한 것으로, 상세 내용은 장례식장 이용계약서 및 약관을 참조하시기 바랍니다.",
                style = TextStyle(fontSize = 11.sp, color = ColorSub, lineHeight = 18.sp),
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
                    .background(ColorConfirmBg, RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFFD4C200), RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "본인은 보람상조개발(주)가 운영하는 보람인천장례식장에서 장례행사 서비스를 이용하기 위한 계약을 체결하기 전에,",
                    style = TextStyle(fontSize = 12.sp, color = ColorText, lineHeight = 20.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                confirmCheckItems.forEach { item ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = item,
                            style = TextStyle(fontSize = 12.sp, color = ColorText, lineHeight = 20.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "위 사항들을 상담사로부터 충분히 설명을 듣고 계약을 체결하였으며,\n계약서 및 약관을 교부받았음을 확인합니다.",
                    style = TextStyle(fontSize = 12.sp, color = ColorText, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
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
            Text("${year}년", style = TextStyle(fontSize = 16.sp))
            Spacer(modifier = Modifier.width(16.dp))
            HandwrittenDateUnit(width = 80.dp)
            Text("월", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))
            HandwrittenDateUnit(width = 80.dp)
            Text("일", modifier = Modifier.padding(horizontal = 8.dp), style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 임대인 / 임차인
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // 임차인 서명
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "계약자(이용자)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 48.dp)
                        .background(Color(0xFFF5F5F5))
                        .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(4.dp))
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
                            Text("성명 (인)", fontSize = 12.sp, color = Color.Gray)
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
            .border(1.dp, ColorBorder)
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTableHead)
                .height(IntrinsicSize.Min)
        ) {
            TableCell("No", 0.6f, isHeader = true)
            VLine()
            TableCell("계약 주요 내용 안내", 3f, isHeader = true)
            VLine()
            TableCell("참 조", 2.5f, isHeader = true)
            VLine()
            TableCell("설명 여부", 2f, isHeader = true)
        }

        // 데이터 행
        confirmRows.forEachIndexed { idx, row ->
            Divider(color = ColorBorder, thickness = 1.dp)
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
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary,
                            textAlign = TextAlign.Center
                        )
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
                        style = TextStyle(fontSize = 12.sp, color = ColorText, lineHeight = 19.sp)
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
                        style = TextStyle(fontSize = 11.sp, color = ColorSub, lineHeight = 17.sp)
                    )
                }
                VLine()
                // 설명 여부 — 펜 직접 입력
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(56.dp)
                ) {
                    HandwrittenAreaCell(
                        modifier = Modifier.fillMaxSize(),
                        externalTick = ticks.getOrElse(idx) { 0 },
                        onDraw = { onDraw(idx) },
                        hint="설명 들었음",
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
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrimary
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 11.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = ColorText,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .weight(weight)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
private fun RowScope.VLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(ColorBorder)
    )
}