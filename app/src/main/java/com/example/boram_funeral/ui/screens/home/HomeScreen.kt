package com.example.boram_funeral.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.components.contracts.funeral.CasketShroudStep
import com.example.boram_funeral.ui.components.contracts.funeral.ReceptionBasicStep
import com.example.boram_funeral.ui.components.contracts.funeral.UseContractStepContent
import com.example.boram_funeral.ui.components.contracts.funeral.UseContractTableInputTest
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var isDialogOpen by remember { mutableStateOf(false) }

    val contractSteps = listOf<@Composable () -> Unit>(
        { ReceptionBasicStep() },           // Index 0
        { UseContractTableInputTest() },     // Index 1
        { CasketShroudStep() },           // Index 2
    )

    // 2. 리스트의 크기를 기반으로 pagerState 생성 (자동으로 11이 됨)
    val pagerState = rememberPagerState(pageCount = { contractSteps.size })
    val scope = rememberCoroutineScope()

// 3. 마지막 페이지 판정도 자동 계산
    val isLastPage by remember {
        derivedStateOf { pagerState.currentPage == contractSteps.size - 1 }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(text = "메인");

        Button(onClick = { isDialogOpen = true }) {
            Text("모달 열기")
        }

        ModalSheet(
            isOpen = isDialogOpen,
            onDismiss = { isDialogOpen = false },
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = { isDialogOpen = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
                }

                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    UseContractStepContent(
                        pagerState = pagerState,
                        contractSteps = contractSteps,
                        onClose = { isDialogOpen = false },
                        onFinish = { isDialogOpen = false }
                    )
                }
                // 하단 네비게이션 버튼 (11단계 상담 프로세스 대응)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
                ) {
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
        }
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
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. '이전' 버튼 영역: 첫 페이지(0)가 아닐 때만 노출
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Large,
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
                    size = ButtonSize.Large,
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