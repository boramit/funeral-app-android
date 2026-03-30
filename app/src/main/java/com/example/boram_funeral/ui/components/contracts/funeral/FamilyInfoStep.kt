package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.components.contracts.funeral.base.HandwrittenAreaCell

// ── 색상 ──────────────────────────────────────────────────────────────────────

private val ColorPrimary   = Color(0xFF05195F)
private val ColorBg        = Color(0xFFF9F9F9)
private val ColorBorder    = Color(0xFF888888)
private val ColorTableHead = Color(0xFFEEF2FF)
private val ColorText      = Color(0xFF222222)
private val ColorSub       = Color(0xFF555555)

// ── 메인 Screen ───────────────────────────────────────────────────────────────

@Composable
fun FamilyInfoStep(viewModel: ContractViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val ticks = uiState.familyTicks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {

        // ── 상단 호실 / 고인명 ─────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, ColorBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("(", style = TextStyle(fontSize = 15.sp, color = ColorText))
            Text(
                text = uiState.roomName.ifEmpty { "  -  " },
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "호실 )",
                style = TextStyle(fontSize = 15.sp, color = ColorText)
            )

            Spacer(modifier = Modifier.width(32.dp))

            Text("故 ", style = TextStyle(fontSize = 15.sp, color = ColorText))
            Text(
                text = uiState.deceasedName.ifEmpty { "  -  " },
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(" 님", style = TextStyle(fontSize = 15.sp, color = ColorText))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── 유가족 정보 테이블 ────────────────────────────────────────────────
        FamilyTable(
            ticks = ticks,
            onChiefMourner  = { viewModel.tickChiefMourner(it) },
            onDaughterInLaw = { viewModel.tickDaughterInLaw(it) },
            onEtc           = { viewModel.tickEtc(it) },
            onDaughter      = { viewModel.tickDaughter(it) },
            onSonInLaw      = { viewModel.tickSonInLaw(it) },
            onExtra1        = { viewModel.tickExtra1(it) },
            onExtra2        = { viewModel.tickExtra2(it) },
            onExtra3        = { viewModel.tickExtra3(it) },
            onExtra4        = { viewModel.tickExtra4(it) }
        )
    }
}

// ── 유가족 정보 테이블 ────────────────────────────────────────────────────────

@Composable
private fun FamilyTable(
    ticks: com.example.boram_funeral.ui.screens.contract.model.FamilyTicks,
    onChiefMourner:  (Int) -> Unit,
    onDaughterInLaw: (Int) -> Unit,
    onEtc:           (Int) -> Unit,
    onDaughter:      (Int) -> Unit,
    onSonInLaw:      (Int) -> Unit,
    onExtra1:        (Int) -> Unit,
    onExtra2:        (Int) -> Unit,
    onExtra3:        (Int) -> Unit,
    onExtra4:        (Int) -> Unit,
) {
    data class CategoryData(val label: String, val ticks: List<Int>, val onDraw: (Int) -> Unit)

    // 9개 구분 — 3열 × 3행
    // 사위 이후 4개는 라벨 빈값
    val categories = listOf(
        CategoryData("상\n\n\n\n주", ticks.chiefMournerTicks,  onChiefMourner),
        CategoryData("자\n\n\n\n부", ticks.daughterInLawTicks, onDaughterInLaw),
        CategoryData("기\n\n\n\n타", ticks.etcTicks,           onEtc),
        CategoryData("여\n\n\n\n식", ticks.daughterTicks,      onDaughter),
        CategoryData("사\n\n\n\n위", ticks.sonInLawTicks,      onSonInLaw),
        CategoryData("",             ticks.extra1Ticks,        onExtra1),
        CategoryData("",             ticks.extra2Ticks,        onExtra2),
        CategoryData("",             ticks.extra3Ticks,        onExtra3),
        CategoryData("",             ticks.extra4Ticks,        onExtra4),
    )

    // 3개씩 나눠서 행으로 구성
    val rows = categories.chunked(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorBorder)
    ) {
        rows.forEachIndexed { rowIdx, rowCategories ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                rowCategories.forEachIndexed { colIdx, category ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                    ) {
                        // 구분 헤더 (빈 라벨이면 빈 헤더 셀)
                        CategoryHeader(label = category.label)
                        VLine()

                        // 6개 입력 칸
                        Column(modifier = Modifier.weight(1f)) {
                            repeat(6) { cellIdx ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    NameLabel()
                                    VLine()
                                    WritingCell(
                                        tick = category.ticks.getOrElse(cellIdx) { 0 },
                                        weight = 1f,
                                        onDraw = { category.onDraw(cellIdx) }
                                    )
                                }
                                if (cellIdx < 5) Divider(color = ColorBorder, thickness = 1.dp)
                            }
                        }
                    }
                    // 열 사이 구분선 (마지막 열 제외)
                    if (colIdx < rowCategories.lastIndex) VLine()
                }
            }
            // 행 사이 구분선 (마지막 행 제외)
            if (rowIdx < rows.lastIndex) Divider(color = ColorBorder, thickness = 1.dp)
        }
    }
}

// ── 유틸 Composable ───────────────────────────────────────────────────────────

/** 구분 헤더 셀 — 모두 동일 width(28.dp), 빈 라벨이면 배경만 표시 */
@Composable
private fun RowScope.CategoryHeader(label: String) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(ColorTableHead),
        contentAlignment = Alignment.Center
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

/** "성명 :" 라벨 */
@Composable
private fun RowScope.NameLabel() {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(42.dp)
            .padding(start = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "성명 :",
            style = TextStyle(fontSize = 10.sp, color = ColorSub)
        )
    }
}

/** 펜 입력 셀 */
@Composable
private fun RowScope.WritingCell(
    tick: Int,
    weight: Float,
    onDraw: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(42.dp)
    ) {
        HandwrittenAreaCell(
            modifier = Modifier.fillMaxSize(),
            externalTick = tick,
            onDraw = onDraw
        )
    }
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