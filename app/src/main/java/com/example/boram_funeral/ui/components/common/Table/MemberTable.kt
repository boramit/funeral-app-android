package com.example.boram_funeral.ui.components.common.Table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel
import com.example.boram_funeral.ui.screens.member.logic.MemberViewModel.EventItem
import com.example.boram_funeral.ui.theme.boram_Br_Color
import com.example.boram_funeral.ui.utils.isLandscape

// 열 너비 상수
private val COL_NO     = 40.dp
private val COL_STATUS = 72.dp   // 상태 뱃지 열 (진행 / 완료 — 2글자)
private val COL_DETAIL = 84.dp   // 비고 버튼 열 (자세히 — 3글자)

/**
 * 행사 목록을 표시하는 테이블 컴포넌트.
 *
 * 헤더 행(고정)과 데이터 행(스크롤 가능)으로 구성된다.
 * 각 행의 "자세히" 버튼 클릭 시 [onEventClick]에 행사 ID를 전달한다.
 *
 * @param events 표시할 행사 목록
 * @param onEventClick 행 클릭 시 호출되는 콜백 — 행사 ID 전달
 * @param modifier 외부에서 주입하는 Modifier
 */
@Composable
fun MemberTable(
    events: List<EventItem>,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 가로 모드: 화면이 넓으므로 1f(균등), 세로 모드: 장례식장 이름이 길어 2f(2배 너비)
    val funeralHomeWeight = if (isLandscape()) 1f else 2f

    Column(modifier = modifier.fillMaxWidth()) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F9FA))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderCell(text = "NO",     modifier = Modifier.width(COL_NO))
            HeaderCell(text = "장례식장", modifier = Modifier.weight(funeralHomeWeight))
            HeaderCell(text = "고인명",  modifier = Modifier.weight(1f))
            HeaderCell(text = "안치일자", modifier = Modifier.weight(1f))
            HeaderCell(text = "입실일자", modifier = Modifier.weight(1f))
            HeaderCell(text = "퇴실일자", modifier = Modifier.weight(1f))
            HeaderCell(text = "발인일자", modifier = Modifier.weight(1f))
            HeaderCell(text = "상태",   modifier = Modifier.width(COL_STATUS))
            HeaderCell(text = "비고",   modifier = Modifier.width(COL_DETAIL))
        }

        // 데이터 행 목록
        LazyColumn {
            items(items = events, key = { it.id }) { event ->
                EventRow(event = event, funeralHomeWeight = funeralHomeWeight, onEventClick = onEventClick)
                HorizontalDivider(color = Color(0xFFF1F1F1))
            }
        }
    }
}

// ── Private Composables ───────────────────────────────────────────────────────

/**
 * 테이블 헤더 셀.
 *
 * @param text 표시할 텍스트
 * @param modifier 열 너비 등을 지정하는 Modifier
 */
@Composable
private fun HeaderCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = if (isLandscape()) 14.sp else 12.sp,
        textAlign = TextAlign.Center,
    )
}

/**
 * 테이블 데이터 셀.
 *
 * 항상 한 줄로 표시하며, 공간이 부족할 경우 말줄임표(…)로 처리한다.
 *
 * @param text 표시할 텍스트
 * @param modifier 열 너비·weight 등을 지정하는 Modifier
 * @param color 텍스트 색상 (기본값: 테마 기본색)
 */
@Composable
private fun DataCell(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 14.sp,
        color = color,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * 행사 상태를 나타내는 뱃지 (진행 / 완료).
 *
 * @param status 행사 진행 상태 enum
 * @param modifier 열 너비 등을 지정하는 Modifier
 */
@Composable
private fun StatusBadge(
    status: MemberViewModel.EventStatus,
    modifier: Modifier = Modifier,
) {
    val (bgColor, textColor) = statusColors(status)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(color = bgColor, shape = RoundedCornerShape(4.dp)) {
            Text(
                text = status.label,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                fontSize = if (isLandscape()) 14.sp else 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * 행사 상세 진입 버튼 ("자세히").
 *
 * @param onClick 클릭 콜백
 * @param modifier 열 너비 등을 지정하는 Modifier
 */
@Composable
private fun DetailButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(
            onClick = onClick,
            color = boram_Br_Color,
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(
                text = "자세히",
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                fontSize = if (isLandscape()) 14.sp else 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * 행사 데이터 한 행.
 *
 * @param event 표시할 행사 데이터
 * @param funeralHomeWeight 장례식장 열의 weight — 세로 모드 2f, 가로 모드 1f
 * @param onEventClick 자세히 버튼 클릭 시 행사 ID를 전달하는 콜백
 */
@Composable
private fun EventRow(
    event: EventItem,
    funeralHomeWeight: Float,
    onEventClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DataCell(text = event.id,              modifier = Modifier.width(COL_NO))
        DataCell(text = event.funeralHomeName, modifier = Modifier.weight(funeralHomeWeight))
        DataCell(text = event.deceasedName,    modifier = Modifier.weight(1f))
        DataCell(text = event.burialDate,      modifier = Modifier.weight(1f), color = Color.Gray)
        DataCell(text = event.checkInDate,     modifier = Modifier.weight(1f), color = Color.Gray)
        DataCell(text = event.checkOutDate,    modifier = Modifier.weight(1f), color = Color.Gray)
        DataCell(text = event.departureDate,   modifier = Modifier.weight(1f), color = Color.Gray)
        StatusBadge(status = event.status,     modifier = Modifier.width(COL_STATUS))
        DetailButton(onClick = { onEventClick(event.id) }, modifier = Modifier.width(COL_DETAIL))
    }
}

// ── Private Helpers ───────────────────────────────────────────────────────────

/**
 * 진행 상태에 따른 배경색·텍스트색 쌍을 반환한다.
 *
 * @return `bgColor to textColor` 쌍
 */
private fun statusColors(status: MemberViewModel.EventStatus): Pair<Color, Color> = when (status) {
    MemberViewModel.EventStatus.ONGOING   -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
    MemberViewModel.EventStatus.COMPLETED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
}
