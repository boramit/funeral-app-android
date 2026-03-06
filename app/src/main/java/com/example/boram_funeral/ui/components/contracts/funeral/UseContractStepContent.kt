package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.ExperimentalFoundationApi
// 1. Compose 기본 UI 및 레이아웃 관련
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// 2. Compose 디자인 수치 및 스타일 관련
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import kotlinx.coroutines.launch

@Composable
fun StepLayout(
    stepNumber: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 상단 헤더: [Step 01] 제목 형태
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 개발 완료 후 이 Text 부분만 주석 처리하면 됩니다.
            Surface(
                color = Color.Red.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Step ${String.format("%02d", stepNumber)}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            thickness = 2.dp,
            color = Color.Black
        )

        // 실제 각 페이지의 테이블 내용이 들어가는 구역
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UseContractStepContent(
    onClose: () -> Unit,
    onFinish: () -> Unit
) {

// 1. 사용할 페이지 컴포넌트들을 리스트로 정의
    val contractSteps = listOf<@Composable () -> Unit>(
        { ReceptionBasicStep() },           // Index 0
        { UseContractTableInputTest() },     // Index 1
        { ReceptionBasicStep() },           // Index 2
        { UseContractTableInputTest() },     // Index 3
        { ReceptionBasicStep() },           // Index 4
        { UseContractTableInputTest() },     // Index 5
        { ReceptionBasicStep() },           // Index 6
        { UseContractTableInputTest() },     // Index 7
        { ReceptionBasicStep() },           // Index 8
        { UseContractTableInputTest() },     // Index 9
        { ReceptionBasicStep() }            // Index 10
    )

// 2. 리스트의 크기를 기반으로 pagerState 생성 (자동으로 11이 됨)
    val pagerState = rememberPagerState(pageCount = { contractSteps.size })
    val scope = rememberCoroutineScope()

// 3. 마지막 페이지 판정도 자동 계산
    val isLastPage by remember {
        derivedStateOf { pagerState.currentPage == contractSteps.size - 1 }
    }


    // 2. 전체 레이아웃 (Scaffold를 컴포넌트 내부에서 사용하여 상하단 고정)
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // 커스텀 헤더 (Scaffold 대신 직접 구현하여 충돌 방지)
        ContractStepHeader(
            progress = (pagerState.currentPage + 1).toFloat() / contractSteps.size
        )

        // 메인 컨텐츠 (Pager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            userScrollEnabled = false,
            verticalAlignment = Alignment.Top
        ) { page ->
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                // 리스트에서 해당 순서의 컴포넌트를 실행함
                contractSteps[page].invoke()
            }
        }
        // 하단 네비게이션 버튼 (11단계 상담 프로세스 대응)
        StepBottomBar(
            currentPage = pagerState.currentPage,
            isLastPage = isLastPage, // 자동으로 마지막 페이지인지 체크됨
            onPrev = {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            },
            onNext = {
                if (!isLastPage) {
                    // 마지막 페이지가 아닐 때: 다음 페이지로 이동
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    // 마지막 페이지일 때: 저장 로직 실행
                    android.util.Log.d("ContractStep", "최종 페이지(${pagerState.currentPage}) 도달: 데이터 저장 시작")
                    // saveContractAsPdf()
                }
            }
        )
    }
}

@Composable
fun ContractStepHeader(
    progress: Float,
    activeColor: Color = Color(0xFF6200EE), // 기본값 설정
    trackColor: Color = Color.LightGray,
    strokeWidth: Dp = 2.dp // 프로그레스 바의 두께(사이즈)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 진행 상태 바
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(strokeWidth), // 여기서 높이(사이즈) 조절
            color = activeColor,      // 진행된 부분 색상
            trackColor = trackColor    // 배경 부분 색상
        )
    }
}

@Composable
fun StepBottomBar(
    currentPage: Int,      // 현재 페이지 번호 (0~10)
    isLastPage: Boolean,    // 마지막 페이지 여부 (currentPage == 10)
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. '이전' 버튼 영역: 첫 페이지(0)가 아닐 때만 노출
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Medium,
                    text = "이전",
                    onClick = onPrev,
                    fullWidth = true,
                    enabled = currentPage > 0,
                    // 이전 버튼은 보통 강조를 덜 하므로 테마가 있다면 보조색 권장
                )
            }

            // 2. '다음/저장' 버튼 영역: 항상 노출되되 텍스트만 변경
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Medium,
                    text = if (isLastPage) "저장하기" else "다음",
                    onClick = {
                        if (isLastPage) {
                            // 최종 저장 로직 실행 (상위에서 처리하도록 onNext 호출 혹은 별도 콜백)
                            onNext()
                        } else {
                            // 다음 페이지로 이동
                            onNext()
                        }
                    },
                    fullWidth = true
                )
            }
        }
    }
}