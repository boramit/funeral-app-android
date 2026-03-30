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

// ── 색상 ──────────────────────────────────────────────────────────────────────

private val ColorPrimary   = Color(0xFF05195F)
private val ColorBg        = Color(0xFFF9F9F9)
private val ColorBorder    = Color(0xFF888888)
private val ColorTableHead = Color(0xFFEEF2FF)
private val ColorText      = Color(0xFF222222)
private val ColorSub       = Color(0xFF555555)
private val ColorNotice    = Color(0xFF888888)

// ── 메인 Screen ───────────────────────────────────────────────────────────────

@Composable
fun DeceasedInfoConsentStep(viewModel: ContractViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    // 하단 서명 Path — UI 레이어에서만 관리
    var signatureData by remember { mutableStateOf<Path?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBg)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {

            // ── 제목 ──────────────────────────────────────────────────────────
            Text(
                text = "사망자 정보 제공동의서",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // ── 안내 항목 1~3 ─────────────────────────────────────────────────
            InfoItem("1. 정보를 제공받는 자", "장사시설 설치·운영자")
            InfoItem("2. 정보 제공 목적", "「장사 등에 관한 법률」 제33조의3에 따라 연금·복지 급여의 관리 등")
            InfoItem(
                "3. 정보 제공 대상",
                "보건복지부장관이 사망자 정보 제공이 필요하다고 인정하는 중앙행정기관,\n지방자치단체, 공공기관 등"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── 4. 사망자 인적사항 ────────────────────────────────────────────
            SectionTitle("4. 사망자 인적 사항")
            DeceasedInfoTable(
                uiState = uiState,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── 5. 유족 등의 인적사항 ─────────────────────────────────────────
            SectionTitle("5. 유족 등의 인적사항")
            SurvivorTable(
                survivorRows = uiState.survivorRows,
                viewModel = viewModel
            )

            Text(
                text = "※ 유족 중 1명만 기재 (정보 변동시 추가 기재 가능)",
                style = TextStyle(fontSize = 11.sp, color = ColorNotice),
                modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
            )

            // ── 동의문 ────────────────────────────────────────────────────────
            Text(
                text = "「장사 등에 관한 법률」 제33조의3제3항 및 같은 법 시행규칙 제22조의2에 따른 사망자 정보등록에 대한 사항에 동의합니다.",
                style = TextStyle(fontSize = 13.sp, color = ColorText, lineHeight = 22.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEF2FF))
                    .padding(12.dp)
            )

            Text(
                text = "※ 사망자 정보 등의 제공을 거부할 경우 장사시설 이용이 제한될 수 있습니다.",
                style = TextStyle(fontSize = 11.sp, color = ColorNotice),
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
            )

            // ── 하단 서명 푸터 ────────────────────────────────────────────────
            DeceasedInfoFooter(
                year = "2026",
                updateTick = uiState.deceasedSignatureUpdateTick,
                capturedPath = signatureData,
                onSignatureClick = { viewModel.showDeceasedSignatureDialog() }
            )
        }
    }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    if (uiState.isDeceasedSignatureDialogVisible) {
        SignatureDialog(
            onDismiss = { viewModel.dismissDeceasedSignatureDialog() },
            onConfirm = { path ->
                signatureData = Path().apply { addPath(path) }
                viewModel.confirmDeceasedSignature()
            }
        )
    }
}

@Composable
private fun DeceasedInfoFooter(
    year: String,
    updateTick: Int,
    capturedPath: Path?,
    onSignatureClick: () -> Unit
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.Top) {
                Text("장사시설 설치·운영자 귀하", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("계약자(이용자)", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 48.dp)
                        .background(Color(0xFFF5F5F5))
                        .border(1.dp, ColorBorder, RoundedCornerShape(4.dp))
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
                                    val s = minOf(
                                        size.width / bounds.width,
                                        size.height / bounds.height
                                    ) * 0.8f
                                    withTransform({
                                        translate(center.x, center.y)
                                        scale(s, s, Offset.Zero)
                                        translate(-bounds.center.x, -bounds.center.y)
                                    }) {
                                        drawPath(
                                            path = capturedPath,
                                            color = Color.Black,
                                            style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
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

// ── 4. 사망자 인적사항 테이블 ─────────────────────────────────────────────────

@Composable
private fun DeceasedInfoTable(
    uiState: com.example.boram_funeral.ui.screens.contract.logic.ContractUiState,
    viewModel: ContractViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorBorder)
    ) {
        // ── 헤더 행 ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTableHead)
                .height(IntrinsicSize.Min)
        ) {
            HeaderCell("성명", 1f)
            VLine()
            HeaderCell("성별", 1f)
            VLine()
            HeaderCell("주민등록번호", 1.5f)
            VLine()
            HeaderCell("최종 주민등록 주소", 2f)
            VLine()
            HeaderCell("사망일", 1f)
            VLine()
            // 장사시설 이용일 헤더 — 부터/까지 2행
            Column(
                modifier = Modifier
                    .weight(3.5f)
                    .height(IntrinsicSize.Min)
            ) {
                // 헤더 타이틀
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorTableHead)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "장사시설 이용일",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorText,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        HLine()

        // ── 입력 행 ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 성명
            HandwrittenCell(
                tick = uiState.deceasedNameTick,
                weight = 1f,
                height = 60.dp,
                onDraw = { viewModel.tickDeceasedName() }
            )
            VLine()
            // 성별
            HandwrittenCell(
                tick = uiState.deceasedGenderTick,
                weight = 1f,
                height = 60.dp,
                onDraw = { viewModel.tickDeceasedGender() }
            )
            VLine()
            // 주민등록번호
            HandwrittenCell(
                tick = uiState.deceasedIdNumberTick,
                weight = 1.5f,
                height = 60.dp,
                onDraw = { viewModel.tickDeceasedIdNumber() }
            )
            VLine()
            // 주소
            HandwrittenCell(
                tick = uiState.deceasedAddressTick,
                weight = 2f,
                height = 60.dp,
                onDraw = { viewModel.tickDeceasedAddress() }
            )
            VLine()
            // 사망일
            HandwrittenCell(
                tick = uiState.deceasedDeathDateTick,
                weight = 1f,
                height = 60.dp,
                onDraw = { viewModel.tickDeceasedDeathDate() }
            )
            VLine()
            // 장사시설 이용일 — 부터 / 까지 각각 년·월·일 펜 입력
            Row(
                modifier = Modifier
                    .weight(3.5f)
                    .height(60.dp)
            ) {
                // 부터
                DateTripleCell(
                    yearTick  = uiState.facilityFromYearTick,
                    monthTick = uiState.facilityFromMonthTick,
                    dayTick   = uiState.facilityFromDayTick,
                    onYearDraw  = { viewModel.tickFacilityFromYear() },
                    onMonthDraw = { viewModel.tickFacilityFromMonth() },
                    onDayDraw   = { viewModel.tickFacilityFromDay() },
                    modifier = Modifier.weight(1f),
                    label = "부터"
                )
                VLine()
                // 까지
                DateTripleCell(
                    yearTick  = uiState.facilityToYearTick,
                    monthTick = uiState.facilityToMonthTick,
                    dayTick   = uiState.facilityToDayTick,
                    onYearDraw  = { viewModel.tickFacilityToYear() },
                    onMonthDraw = { viewModel.tickFacilityToMonth() },
                    onDayDraw   = { viewModel.tickFacilityToDay() },
                    modifier = Modifier.weight(1f),
                    label = "까지"
                )
            }
        }
    }
}

// ── 5. 유족 인적사항 테이블 ───────────────────────────────────────────────────

@Composable
private fun SurvivorTable(
    survivorRows: List<com.example.boram_funeral.ui.screens.contract.model.SurvivorRowTick>,
    viewModel: ContractViewModel
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
            HeaderCell("사망자와의 관계", 1.5f)
            VLine()
            HeaderCell("동의자 성명", 1.5f)
            VLine()
            HeaderCell("생년월일", 1.5f)
            VLine()
            HeaderCell("주소", 2.5f)
            VLine()
            HeaderCell("정보제공에 동의함 (성명 또는 인)", 2f)
        }

        // 유족 3행
        survivorRows.forEachIndexed { idx, row ->
            HLine()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                HandwrittenCell(
                    tick = row.relationTick,
                    weight = 1.5f,
                    height = 56.dp,
                    onDraw = { viewModel.tickSurvivorRelation(idx) }
                )
                VLine()
                HandwrittenCell(
                    tick = row.nameTick,
                    weight = 1.5f,
                    height = 56.dp,
                    onDraw = { viewModel.tickSurvivorName(idx) }
                )
                VLine()
                HandwrittenCell(
                    tick = row.birthDateTick,
                    weight = 1.5f,
                    height = 56.dp,
                    onDraw = { viewModel.tickSurvivorBirthDate(idx) }
                )
                VLine()
                HandwrittenCell(
                    tick = row.addressTick,
                    weight = 2.5f,
                    height = 56.dp,
                    onDraw = { viewModel.tickSurvivorAddress(idx) }
                )
                VLine()
                HandwrittenCell(
                    tick = row.signatureTick,
                    weight = 2f,
                    height = 56.dp,
                    onDraw = { viewModel.tickSurvivorSignature(idx) }
                )
            }
        }
    }
}

// ── HandwrittenCell — 테이블 칸 내부 펜 입력 ─────────────────────────────────

@Composable
private fun RowScope.HandwrittenCell(
    tick: Int,
    weight: Float,
    height: androidx.compose.ui.unit.Dp = 56.dp,
    onDraw: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(height)
            .border(0.dp, Color.Transparent)
    ) {
        HandwrittenAreaCell(
            modifier = Modifier.fillMaxSize(),
            externalTick = tick,
            onDraw = onDraw
        )
    }
}

// ── DateTripleCell — 년·월·일 각각 펜 입력 ───────────────────────────────────

@Composable
private fun DateTripleCell(
    yearTick: Int,
    monthTick: Int,
    dayTick: Int,
    onYearDraw: () -> Unit,
    onMonthDraw: () -> Unit,
    onDayDraw: () -> Unit,
    label: String = "",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 년
        Box(modifier = Modifier.weight(1f).height(42.dp)) {
            HandwrittenAreaCell(
                modifier = Modifier.fillMaxSize(),
                externalTick = yearTick,
                onDraw = onYearDraw
            )
        }
        Text("년", fontSize = 10.sp, color = ColorSub, modifier = Modifier.padding(horizontal = 2.dp))

        // 월
        Box(modifier = Modifier.weight(0.5f).height(42.dp)) {
            HandwrittenAreaCell(
                modifier = Modifier.fillMaxSize(),
                externalTick = monthTick,
                onDraw = onMonthDraw
            )
        }
        Text("월", fontSize = 10.sp, color = ColorSub, modifier = Modifier.padding(horizontal = 2.dp))

        // 일
        Box(modifier = Modifier.weight(0.5f).height(42.dp)) {
            HandwrittenAreaCell(
                modifier = Modifier.fillMaxSize(),
                externalTick = dayTick,
                onDraw = onDayDraw
            )
        }
        Text("일", fontSize = 10.sp, color = ColorSub, modifier = Modifier.padding(start = 2.dp))
        // 부터 / 까지 등 라벨
        if (label.isNotEmpty()) {
            Text(
                text = " $label",
                fontSize = 9.sp,
                color = ColorSub,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

// ── 유틸 Composable ───────────────────────────────────────────────────────────

@Composable
private fun InfoItem(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = "$label : ",
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorPrimary)
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 12.sp, color = ColorText, lineHeight = 19.sp)
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorPrimary),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ColorText,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .weight(weight)
            .padding(6.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}

@Composable
private fun VLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(ColorBorder)
    )
}

@Composable
private fun HLine() {
    Divider(color = ColorBorder, thickness = 1.dp)
}