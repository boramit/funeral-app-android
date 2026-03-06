package com.example.boram_funeral.ui.components.common.Tab

// 기본 Compose 및 레이아웃
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSubTabControl(
    selectedSubTab: Int,
    onSubTabSelected: (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val subTabs = listOf("이용계약서", "관", "유골함", "각인","수의","멧베","제단","헌화꽃","장의차량","고깔","양복","개량","셔츠","타이")

    // 1. Pager 상태 및 높이 측정용 상태 선언
    val pagerState = androidx.compose.foundation.pager.rememberPagerState { subTabs.size }

    // 2. 외부 selectedSubTab 변경 시 Pager와 동기화
    LaunchedEffect(selectedSubTab) {
        if (pagerState.currentPage != selectedSubTab) {
            pagerState.animateScrollToPage(selectedSubTab)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onSubTabSelected(pagerState.currentPage)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row () {
            // 탭 버튼 영역
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                    .padding(4.dp), // 사이드바 전체 안쪽 여백
                verticalArrangement = Arrangement.spacedBy(8.dp) // 버튼 사이 간격
            ) {
                subTabs.forEachIndexed { index, title ->
                    val isSelected = (selectedSubTab == index)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Column 내부 공간 100% 채우기
                            .then(
                                if (isSelected) {
                                    Modifier.shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(4.dp),
                                        clip = false,
                                        ambientColor = Color.Black.copy(alpha = 0.2f),
                                        spotColor = Color.Black.copy(alpha = 0.2f)
                                    )
                                } else Modifier
                            )
                            .height(42.dp)
                            .background(
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, // 클릭 시 깜빡임 제거
                                onClick = { onSubTabSelected(index) }
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color(0xFF666666)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 4. 컨텐츠 영역 (측정된 높이 적용)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer(clip = false) // 그림자 잘림 방지
                    .heightIn(max = 900.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.Top,
                userScrollEnabled = false,
            ) { pageIndex ->
                Box(modifier = Modifier.wrapContentHeight()) {
                    content(pageIndex)
                }
            }
        }
    }
}