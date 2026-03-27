package com.example.boram_funeral.ui.screens.contract

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import android.util.Log
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.contracts.funeral.*
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.funeralcontract.ui.FuneralContractStep

@Composable
fun ContractScreen(
    onDismiss: () -> Unit,
    contractViewModel: ContractViewModel,  // ← 상위에서 주입
) {

    // ─── Step 리스트 ──────────────────────────────────────
    val contractSteps = listOf<@Composable () -> Unit>(
        { ReceptionBasicStep(viewModel = contractViewModel) },
        { DeceasedDetailStep(viewModel = contractViewModel) },
        { CasketShroudStep(viewModel = contractViewModel) },
        { FoodCateringStep() },
        { FuneralContractStep(viewModel = contractViewModel) },
    )

    // ─── 페이징 상태 ──────────────────────────────────────
    val pagerState = rememberPagerState(pageCount = { contractSteps.size })
    val scope = rememberCoroutineScope()
    val isLastPage by remember {
        derivedStateOf { pagerState.currentPage == contractSteps.size - 1 }
    }

    // ─── 레이아웃 ─────────────────────────────────────────
    Column(modifier = Modifier.fillMaxSize()) {

        IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            UseContractStepContent(
                pagerState = pagerState,
                contractSteps = contractSteps,
                onClose = onDismiss,
                onFinish = onDismiss
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
        ) {
            StepBottomBar(
                currentPage = pagerState.currentPage,
                isLastPage = isLastPage,
                onPrev = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                onNext = {
                    if (!isLastPage) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        contractViewModel.saveData {
                            onDismiss()
                        }
                    }
                }
            )
        }
    }
}

// ─── StepBottomBar (HomeScreen에서 이동) ──────────────────
@Composable
fun StepBottomBar(
    currentPage: Int,
    isLastPage: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Large,
                    text = "이전",
                    onClick = onPrev,
                    fullWidth = true,
                    enabled = currentPage > 0,
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                CustomButton(
                    size = ButtonSize.Large,
                    text = if (isLastPage) "저장하기" else "다음",
                    onClick = onNext,
                    fullWidth = true
                )
            }
        }
    }
}