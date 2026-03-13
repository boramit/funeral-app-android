package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.ExperimentalFoundationApi
// 1. Compose 기본 UI 및 레이아웃 관련
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
    pagerState: PagerState,
    contractSteps: List<@Composable () -> Unit>,
    onClose: () -> Unit,
    onFinish: () -> Unit
) {
    // 2. 전체 레이아웃 (Scaffold를 컴포넌트 내부에서 사용하여 상하단 고정)
    Column(modifier = Modifier.fillMaxSize()) {
        // 커스텀 헤더 (Scaffold 대신 직접 구현하여 충돌 방지)
        ContractStepHeader(
            progress = (pagerState.currentPage + 1).toFloat() / contractSteps.size
        )

        // 메인 컨텐츠 (Pager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            userScrollEnabled = false,
        ) { page ->
            contractSteps[page].invoke()
        }
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
