package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

// ── 색상 ──────────────────────────────────────────────────────────────────────

private val ColorHeader     = Color(0xFF5B9BD5)
private val ColorRowEven    = Color(0xFFFFFFFF)
private val ColorRowOdd     = Color(0xFFF0F4FA)
private val ColorDivider    = Color(0xFFCCCCCC)
private val ColorTitle      = Color(0xFF1A1A2E)
private val ColorText       = Color(0xFF333333)
private val ColorNote       = Color(0xFF555555)
private val ColorNoticeBg   = Color(0xFFFFF8E1)
private val ColorNoticeTitle= Color(0xFF795548)
private val ColorNoticeText = Color(0xFF5D4037)
private val ColorBg           = Color(0xFFF5F5F5)
private val ColorFlowHeader   = Color(0xFF1C2D6E)
private val ColorFlowBox      = Color(0xFFFFFFFF)
private val ColorFlowBorder   = Color(0xFF888888)
private val ColorFlowArrow    = Color(0xFF444444)
private val ColorFlowMiddleBg = Color(0xFFF5CBA7)

// ── 메인 Composable ───────────────────────────────────────────────────────────

@Composable
fun CeremonyOrderStep() {
    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 제목
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "장례의식 순서",
                style = TextStyle(
                    color = ColorTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // 테이블
        CeremonyTable()

        Spacer(modifier = Modifier.height(24.dp))

        // 진행 절차 플로우차트
        CeremonyFlowSection()

        Spacer(modifier = Modifier.height(16.dp))

        // 주의사항
        NoticeSection()
    }
}

// ── 테이블 ────────────────────────────────────────────────────────────────────

@Composable
private fun CeremonyTable() {
    val mergedNote = ceremonyData.firstOrNull { it.note.isNotEmpty() }?.note ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorRowEven, RoundedCornerShape(8.dp))
    ) {
        // 헤더
        TableHeader()

        Divider(color = ColorDivider, thickness = 1.dp)

        // 본문: 구분+주요업무 열 / 비고 열(병합)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 좌측: 구분 + 주요업무 (행별 분리)
            Column(modifier = Modifier.weight(3.5f)) {
                ceremonyData.forEachIndexed { index, item ->
                    TableDataRow(item = item, index = index)
                    if (index < ceremonyData.lastIndex) {
                        Divider(color = ColorDivider, thickness = 1.dp)
                    }
                }
            }

            // 구분선
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorDivider)
            )

            // 우측: 비고 (전체 행 병합)
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(
                        color = ColorRowEven,
                        shape = RoundedCornerShape(bottomEnd = 8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mergedNote,
                    style = TextStyle(color = ColorNote, fontSize = 12.sp),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorHeader, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .height(IntrinsicSize.Min)
    ) {
        HeaderCell(text = "구분", weight = 1f)
        VerticalDivider()
        HeaderCell(text = "주요업무(약식)", weight = 2.5f)
        VerticalDivider()
        HeaderCell(text = "비고", weight = 2f)
    }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun TableDataRow(item: DayItem, index: Int) {
    val bgColor = if (index % 2 == 0) ColorRowEven else ColorRowOdd

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor)
            .height(IntrinsicSize.Min)
    ) {
        // 구분
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.day,
                style = TextStyle(
                    color = ColorTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        VerticalDivider()

        // 주요업무
        Column(
            modifier = Modifier
                .weight(2.5f)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            item.tasks.forEachIndexed { i, task ->
                Text(
                    text = "${i + 1}. $task",
                    style = TextStyle(color = ColorText, fontSize = 13.sp),
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

        // 제목
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "장례의식의 진행 절차",
                style = TextStyle(
                    color = ColorTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3열 플로우
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            flowData.forEach { day ->
                FlowDayColumn(day = day, modifier = Modifier.weight(1f))
            }
        }

        // 첫째 날 → 둘째 날 안내 텍스트
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "※ 첫째 날 하는 경우도 있음",
            style = TextStyle(color = Color(0xFF888888), fontSize = 11.sp),
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
                .background(ColorFlowHeader, RoundedCornerShape(4.dp))
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
    val borderColor = if (isDotted) Color(0xFFD1D1D1) else ColorFlowBorder
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ColorFlowBox,
                shape = RoundedCornerShape(4.dp)
            )
            .then(
                if (isDotted) Modifier.dashedBorder(borderColor)
                else Modifier.border(1.dp, borderColor, RoundedCornerShape(4.dp))
            )
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = ColorText,
                fontSize = 12.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlowArrow() {
    Box(
        modifier = Modifier
            .width(2.dp)
            .height(16.dp)
            .background(ColorFlowArrow)
    )
}

// 점선 테두리 Modifier 확장
private fun Modifier.dashedBorder(color: Color): Modifier = this.then(
    Modifier.background(Color.Transparent, RoundedCornerShape(4.dp))
        .padding(1.dp)
        .background(Color(0xFFFFEBEE), RoundedCornerShape(3.dp))
)

// ── 주의사항 ──────────────────────────────────────────────────────────────────

@Composable
private fun NoticeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorNoticeBg, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "※ 주의사항 ※",
            style = TextStyle(
                color = ColorNoticeTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        notices.forEachIndexed { i, notice ->
            Text(
                text = "${i + 1}. $notice",
                style = TextStyle(
                    color = ColorNoticeText,
                    fontSize = 12.5.sp
                ),
                lineHeight = 19.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

// ── 유틸 ──────────────────────────────────────────────────────────────────────

@Composable
private fun RowScope.VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(ColorDivider)
    )
}