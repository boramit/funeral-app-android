package com.example.boram_funeral.ui.components.common.Tab

// 기본 Compose 및 레이아웃
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

// 탭 및 페이저 관련 (Foundation 레이어)
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow

// 애니메이션 및 코루틴 (탭 클릭 시 스크롤용)
import kotlinx.coroutines.launch

@Composable
fun CustomTabControl(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    contentHeight: Dp = Dp.Unspecified,
    content: @Composable (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            // 탭 바 영역
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                modifier = Modifier.width(200.dp).background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)).padding(2.dp), // 버튼 2개 기준 적절한 너비
                indicator = {}, // 1. 인디케이터(선) 완전 제거
                divider = {}    // 2. 하단 구분선 제거
            ) {

                tabs.forEachIndexed { index, title ->
                    val isSelected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .padding(2.dp)
                            // 1. 선택 시 그림자 설정
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
                            // 2. 배경색 설정
                            .background(
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )

                            // 3. 클릭 이벤트 (깜빡임 제거의 핵심!)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, // 이 한 줄로 모든 깜빡임이 사라집니다.
                                onClick = {
                                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                }
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else Color(0xFF757575)
                            )
                        )
                    }


                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // 탭과 컨텐츠 사이 간격

        // 컨텐츠 영역 (Pager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
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