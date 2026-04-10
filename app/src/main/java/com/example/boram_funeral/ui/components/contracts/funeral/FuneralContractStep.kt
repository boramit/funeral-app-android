package com.example.funeralcontract.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.MiniInputCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import androidx.compose.ui.graphics.Path
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureDialog
import com.example.boram_funeral.ui.screens.contract.model.FuneralItem
import java.text.NumberFormat
import java.util.Locale

// 다른 계약서 공통 색상
private val COLOR_BRAND   = Color(0xFF05195F)   // 브랜드 다크 네이비
private val COLOR_HEADER  = Color(0xFFF5F5F5)   // 헤더/레이블 배경
private val COLOR_BORDER  = Color(0xFFD1D1D1)   // 테두리
private val COLOR_ACCENT  = Color(0xFFE1E9F5)   // 강조 셀 (최종합계)

@Composable
fun FuneralContractStep(viewModel: ContractViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var showSettingSignDialog by remember { mutableStateOf(false) }
    var showReturnSignDialog  by remember { mutableStateOf(false) }
    var settingSignature      by remember { mutableStateOf<Path?>(null) }
    var returnSignature       by remember { mutableStateOf<Path?>(null) }

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
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
                    text = "매점용품리스트",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = COLOR_BRAND
                )
            }

            // ── 빈소 / 고인 / 일자 정보 ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, COLOR_BORDER),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 빈소
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("빈소 :", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND)
                    Text(uiState.roomName, fontSize = 16.sp)
                }
                // 고인명
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("故 :", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND)
                    Text(uiState.deceasedName, fontSize = 16.sp)
                }
                // 일자
                Row(
                    modifier = Modifier.weight(2f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("일 자 :", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND)
                    Text(uiState.year + "년", fontSize = 16.sp)
                    MiniInputCell(value = uiState.contractStartMonth, onValueChange = viewModel::updateContractStartMonth, width = 28.dp)
                    Text("월", fontSize = 16.sp)
                    MiniInputCell(value = uiState.contractStartDay, onValueChange = viewModel::updateContractStartDay, width = 28.dp)
                    Text("일 ~", fontSize = 16.sp)
                    MiniInputCell(value = uiState.contractEndMonth, onValueChange = viewModel::updateContractEndMonth, width = 28.dp)
                    Text("월", fontSize = 16.sp)
                    MiniInputCell(value = uiState.contractEndDay, onValueChange = viewModel::updateContractEndDay, width = 28.dp)
                    Text("일", fontSize = 16.sp)
                }
            }

            // ── 안내 문구 ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, COLOR_BORDER)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "※ 음료, 주류는 낱개반품가능 그외의 잡화는 개봉시 반품불가입니다.",
                    fontSize = 12.sp,
                    color = COLOR_BRAND
                )
            }

            // ── 테이블 헤더 ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(COLOR_HEADER)
                    .border(0.5.dp, COLOR_BORDER),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContractHeaderCell("번호",     weight = 0.5f)
                ContractHeaderCell("품명",     weight = 2f)
                ContractHeaderCell("단가",     weight = 1f)
                ContractHeaderCell("셋팅수량", weight = 0.8f)
                ContractHeaderCell("반품수량", weight = 0.8f)
                ContractHeaderCell("금액",     weight = 1.2f)
            }

            // ── 품목 리스트 ───────────────────────────────────────────────────
            uiState.items.forEach { item ->
                FuneralItemRow(
                    item = item,
                    onReturnQtyChange = { qty -> viewModel.updateReturnQuantity(item.number, qty) }
                )
            }

            // ── 합계 / 서명 행 ────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(3f)) {
                    // 셋팅확인
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(0.5.dp, COLOR_BORDER)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(COLOR_HEADER)
                                .border(0.5.dp, COLOR_BORDER)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("셋팅확인", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        SignatureArea(
                            label = "셋팅확인",
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                        )
                    }
                    // 반품확인
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(0.5.dp, COLOR_BORDER)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(COLOR_HEADER)
                                .border(0.5.dp, COLOR_BORDER)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("반품확인", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        SignatureArea(
                            label = "반품확인",
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                        )
                    }
                }
                // 합계
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .height(120.dp)
                        .border(0.5.dp, COLOR_BORDER),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("합  계", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formatWon(uiState.totalAmount),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = COLOR_BRAND
                    )
                }
            }

            // ── 금액 합계 정보 ────────────────────────────────────────────────
            // 셋팅 합계
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, COLOR_BORDER)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(COLOR_HEADER)
                        .border(0.5.dp, COLOR_BORDER)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("셋팅 합계", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(formatWon(uiState.settingAmount), fontSize = 13.sp)
                }
            }
            // 반품 차감
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, COLOR_BORDER)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(COLOR_HEADER)
                        .border(0.5.dp, COLOR_BORDER)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("반품 차감", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text("- ${formatWon(uiState.returnAmount)}", fontSize = 13.sp)
                }
            }
            // 최종 합계
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, COLOR_BORDER)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(COLOR_ACCENT)
                        .border(0.5.dp, COLOR_BORDER)
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("최종 합계", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = COLOR_BRAND)
                }
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .background(COLOR_ACCENT)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = formatWon(uiState.totalAmount),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = COLOR_BRAND
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showSettingSignDialog) {
        SignatureDialog(
            onDismiss = { showSettingSignDialog = false },
            onConfirm = { path ->
                settingSignature = Path().apply { addPath(path) }
                showSettingSignDialog = false
            }
        )
    }
    if (showReturnSignDialog) {
        SignatureDialog(
            onDismiss = { showReturnSignDialog = false },
            onConfirm = { path ->
                returnSignature = Path().apply { addPath(path) }
                showReturnSignDialog = false
            }
        )
    }
}

// ── 테이블 헤더 셀 ───────────────────────────────────────────────────────────
@Composable
private fun RowScope.ContractHeaderCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .border(0.5.dp, COLOR_BORDER)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = COLOR_BRAND,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ── 품목 행 ──────────────────────────────────────────────────────────────────
@Composable
private fun FuneralItemRow(
    item: FuneralItem,
    onReturnQtyChange: (Int) -> Unit
) {
    var inputText by remember(item.returnQuantity) {
        mutableStateOf(if (item.returnQuantity == 0) "" else item.returnQuantity.toString())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 번호
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER),
            contentAlignment = Alignment.Center
        ) {
            Text(item.number.toString(), fontSize = 16.sp, color = Color.Black)
        }
        // 품명
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(item.name, fontSize = 16.sp)
        }
        // 단가
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER)
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(formatWon(item.unitPrice), fontSize = 16.sp, color = Color.Black)
        }
        // 셋팅수량
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .background(COLOR_HEADER)
                .border(0.5.dp, COLOR_BORDER),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.settingQuantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = COLOR_BRAND
            )
        }
        // 반품수량
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER),
            contentAlignment = Alignment.Center
        ) {
            MiniInputCell(
                value = inputText,
                onValueChange = { raw ->
                    val digits = raw.filter { it.isDigit() }
                    inputText = digits
                    onReturnQtyChange(digits.toIntOrNull() ?: 0)
                },
                width = 44.dp,
            )
        }
        // 금액
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .border(0.5.dp, COLOR_BORDER)
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = formatWon(item.amount),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

// ── 유틸 ─────────────────────────────────────────────────────────────────────
private fun formatWon(amount: Long): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(amount) + "원"
