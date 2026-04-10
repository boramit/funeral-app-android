package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── 색상 ──────────────────────────────────────────────────────────────────────
private val COLOR_BRAND  = Color(0xFF05195F)
private val COLOR_HEADER = Color(0xFFF5F5F5)
private val COLOR_BORDER = Color(0xFFD1D1D1)

// ── 데이터 ────────────────────────────────────────────────────────────────────

private data class DayItem(
    val day: String,
    val tasks: List<String>,
    val note: String = ""
)

private val ceremonyData = listOf(
    DayItem(
        day = "1일차",
        tasks = listOf(
            "고인 이송 및 안치 확인(유족 안내)",
            "수시",
            "각종 상담 업무 및 직무 분담",
            "빈소설치",
            "부고 안내 및 절차 안내",
            "종교별 장례 지도",
            "장법에 따른 조치",
            "상식에 대한 안내 및 진행"
        ),
        note = "※ 각종 상담(상품, 장례절차, 제의례 절차 등)이 완료되면 그에 따른 과정을 직접적으로 진행하고 유족을 안내한다. 또한 조배상, 상식, 성복, 발인 등 제·의례의식을 진행하고 유족을 돕는다."
    ),
    DayItem(
        day = "2일차",
        tasks = listOf(
            "염습 및 입관 안내",
            "성복(성복제 등) 및 발인 일정 안내"
        )
    ),
    DayItem(
        day = "3일차",
        tasks = listOf(
            "정산 및 발인",
            "운구 인원 확인 및 발인 동선 확인"
        )
    )
)

// ── 진행 절차 플로우 데이터 ──────────────────────────────────────────────────────

private data class FlowDay(
    val title: String,
    val steps: List<String>,
    val isDottedBorder: Boolean = false
)

private val flowData = listOf(
    FlowDay(
        title = "첫째 날",
        steps = listOf(
            "임종 (행사발생 접수)",
            "장례식장으로 고인 운송",
            "안치실 안치",
            "장례식장 계약",
            "설전(設奠) 영정 모시기",
            "혼백(魂帛) 모시기",
            "부고(訃告)알림"
        )
    ),
    FlowDay(
        title = "둘째 날",
        steps = listOf(
            "습(襲)",
            "소렴(小殮)",
            "대렴(大殮)",
            "입관(入棺)",
            "명정(銘旌) 세우기",
            "성복(成服)",
            "성복제(成服祭)",
            "조문(弔問)"
        ),
        isDottedBorder = true
    ),
    FlowDay(
        title = "셋째 날",
        steps = listOf(
            "발인(發靷)",
            "운구(運柩)",
            "화장(火葬)",
            "납골(納骨)",
            "위령제(慰靈祭)",
            "탈상제(脫喪祭)",
            "초우제(初虞祭)",
            "삼 우 제"
        )
    )
)

private val notices = listOf(
    "위 표는 일반적인 장례식의 진행 절차를 설명한 것으로써 실제 행사 절차는 종교, 지역 풍습, 유족 요청사항 등에 따라 달라질 수 있습니다.",
    "상조회사의 장례서비스를 이용하시는 경우 해당 상조회사로부터 장례의식 진행 절차에 관하여 별도 안내받으시기 바랍니다.",
    "본 장례식장을 통해 장례서비스를 이용받기를 원하시는 고객님께서는, 계약체결을 담당하는 직원에게 문의하여 주시기 바랍니다."
)

// ── 메인 Composable ───────────────────────────────────────────────────────────

@Composable
fun CeremonyOrderStep() {
    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── 타이틀 ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "장례의식 순서",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = COLOR_BRAND
                )
            }

            // ── 테이블 ────────────────────────────────────────────────────────
            CeremonyTable()

            Spacer(modifier = Modifier.height(24.dp))

            // ── 진행 절차 타이틀 ──────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "장례의식의 진행 절차",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = COLOR_BRAND
                )
            }

            // ── 진행 절차 플로우차트 ───────────────────────────────────────────
            CeremonyFlowSection()

            Spacer(modifier = Modifier.height(16.dp))

            // ── 주의사항 ──────────────────────────────────────────────────────
            NoticeSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── 테이블 ────────────────────────────────────────────────────────────────────

@Composable
private fun CeremonyTable() {
    val mergedNote = ceremonyData.firstOrNull { it.note.isNotEmpty() }?.note ?: ""

    Column(modifier = Modifier.fillMaxWidth()) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(COLOR_HEADER)
                .border(0.5.dp, COLOR_BORDER),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CeremonyHeaderCell("구분",         weight = 1f)
            CeremonyHeaderCell("주요업무(약식)", weight = 2.5f)
            CeremonyHeaderCell("비고",         weight = 2f)
        }

        // 본문
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .border(0.5.dp, COLOR_BORDER)
        ) {
            // 좌측: 구분 + 주요업무
            Column(modifier = Modifier.weight(3.5f)) {
                ceremonyData.forEachIndexed { index, item ->
                    TableDataRow(item = item)
                    if (index < ceremonyData.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(COLOR_BORDER)
                        )
                    }
                }
            }

            // 우측: 비고 (병합)
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .border(0.5.dp, COLOR_BORDER)
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mergedNote,
                    fontSize = 12.sp,
                    color = Color(0xFF333333),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun RowScope.CeremonyHeaderCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .border(0.5.dp, COLOR_BORDER)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = COLOR_BRAND,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TableDataRow(item: DayItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(IntrinsicSize.Min)
    ) {
        // 구분
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER)
                .background(COLOR_HEADER)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.day,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = COLOR_BRAND
            )
        }

        // 주요업무
        Column(
            modifier = Modifier
                .weight(2.5f)
                .border(0.5.dp, COLOR_BORDER)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            item.tasks.forEachIndexed { i, task ->
                Text(
                    text = "${i + 1}. $task",
                    fontSize = 13.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

// ── 진행 절차 플로우차트 ──────────────────────────────────────────────────────────

@Composable
private fun CeremonyFlowSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 3열 플로우
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            flowData.forEach { day ->
                FlowDayColumn(day = day, modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "※ 첫째 날 하는 경우도 있음",
            fontSize = 14.sp,
            color = Color(0xFF888888),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun FlowDayColumn(day: FlowDay, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(COLOR_BRAND)
                .border(0.5.dp, COLOR_BORDER)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // 단계 박스들
        day.steps.forEachIndexed { index, step ->
            FlowStepBox(text = step, isDotted = day.isDottedBorder)
            if (index < day.steps.lastIndex) {
                FlowArrow()
            }
        }
    }
}

@Composable
private fun FlowStepBox(text: String, isDotted: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(0.5.dp, COLOR_BORDER)
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlowArrow() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(12.dp)
            .background(COLOR_BORDER)
    )
}

// ── 주의사항 ──────────────────────────────────────────────────────────────────

@Composable
private fun NoticeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, COLOR_BORDER)
    ) {
        // 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(COLOR_HEADER)
                .border(0.5.dp, COLOR_BORDER)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "※ 주의사항",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = COLOR_BRAND
            )
        }

        // 내용
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            notices.forEachIndexed { i, notice ->
                Text(
                    text = "${i + 1}. $notice",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }
    }
}
