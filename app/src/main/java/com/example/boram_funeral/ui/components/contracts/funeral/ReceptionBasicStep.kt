package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.SelectCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.contract.model.ContractData

@Composable
fun ReceptionBasicStep(viewModel: ContractViewModel) {

    // ViewModel의 상태를 구독 — 값이 바뀌면 자동으로 화면 재구성
    val uiState by viewModel.uiState.collectAsState()

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로고 + 타이틀
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_funeral_logo_uijeongbu),
                    contentDescription = "Boram Logo",
                    modifier = Modifier.width(300.dp).height(58.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "장례식장 이용계약서",
                    style = TextStyle(
                        color = Color(0xFF05195F),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ── 부고사유 / 행사형태 ───────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("부고사유")
                            SelectCell(
                                selectedOption = uiState.deathReason,
                                onOptionSelected = viewModel::updateDeathReason,
                                options = listOf("병사", "외인사", "자연사", "미상", "기타", "코로나"),
                                weight = 3f,
                                placeholder = "선택"
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("행사형태")
                            SelectCell(
                                selectedOption = uiState.eventType,
                                onOptionSelected = viewModel::updateEventType,
                                options = listOf("자체 행사", "타상조 행사", "보람그룹 행사", "무빈소 행사", "대관 행사"),
                                weight = 3f,
                                placeholder = "선택"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── 유입경로 ─────────────────────────────────────────────
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("유입경로")
                    InputCell(
                        value = uiState.inflowPath,
                        onValueChange = viewModel::updateInflowPath,
                        weight = 3f,
                        placeholder = "경로 입력",
                        alignment = Alignment.CenterStart
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("장례식장")
                    SelectCell(
                        selectedOption = uiState.selectedFuneralHome.displayName,
                        onOptionSelected = { name ->
                            viewModel.updateFuneralHomeByName(name)
                        },
                        options = ContractData.displayNames(),
                        weight = 3f,
                        placeholder = "장례식장 선택"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── 빈소 ─────────────────────────────────────────────────
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("빈소")
                    SelectCell(
                        selectedOption = uiState.roomName,
                        onOptionSelected = viewModel::updateRoomName,
                        options = uiState.roomOptions,  // 장례식장 선택 시 자동 교체
                        weight = 3f,
                        placeholder = "빈소 선택"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── 상주명 / 고인명 / 지도사명 ───────────────────────────
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("상주명")
                    InputCell(
                        value = uiState.chiefMourner,
                        onValueChange = viewModel::updateChiefMourner,
                        weight = 2f,
                        alignment = Alignment.CenterStart
                    )
                    LabelCell("고인명")
                    InputCell(
                        // Step 0에서 입력한 고인명이 Step 1, Step 4 에서도 동일하게 표시됨
                        value = uiState.deceasedName,
                        onValueChange = viewModel::updateDeceasedName,
                        weight = 2f,
                        alignment = Alignment.CenterStart
                    )
                    LabelCell("지도사명")
                    InputCell(
                        value = uiState.directorName,
                        onValueChange = viewModel::updateDirectorName,
                        weight = 2f,
                        alignment = Alignment.CenterStart
                    )
                }
            }
        }
    }
}
