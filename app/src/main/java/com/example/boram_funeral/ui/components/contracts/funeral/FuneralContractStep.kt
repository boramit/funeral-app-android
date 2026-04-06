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
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.MiniInputCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import androidx.compose.ui.graphics.Path
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureDialog
import com.example.boram_funeral.ui.screens.contract.model.FuneralItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FuneralContractStep(viewModel: ContractViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // 서명 상태 — Path는 ViewModel에 넣을 수 없으므로 remember로 관리
    var showSettingSignDialog  by remember { mutableStateOf(false) }
    var showReturnSignDialog   by remember { mutableStateOf(false) }
    var settingSignature       by remember { mutableStateOf<Path?>(null) }
    var returnSignature        by remember { mutableStateOf<Path?>(null) }

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── 타이틀 ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "매점용품리스트",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
            }

            // ── 빈소 / 고인 / 일자 정보 ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(0.5.dp, Color(0xFFD1D1D1))
            ) {
                // 빈소
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("빈 소 :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(uiState.roomName, fontSize = 12.sp)
                }
                // 고인명
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("故 :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(uiState.deceasedName, fontSize = 12.sp)
                }
                // 일자
                Row(
                    modifier = Modifier.weight(2f).padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("일 자 :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(uiState.year + "년", fontSize = 12.sp)
                    MiniInputCell(value = uiState.contractStartMonth, onValueChange = viewModel::updateContractStartMonth, width = 30.dp)
                    Text("월", fontSize = 12.sp)
                    MiniInputCell(value = uiState.contractStartDay, onValueChange = viewModel::updateContractStartDay, width = 30.dp)
                    Text("일 ~", fontSize = 12.sp)
                    MiniInputCell(value = uiState.contractEndMonth, onValueChange = viewModel::updateContractEndMonth, width = 30.dp)
                    Text("월", fontSize = 12.sp)
                    MiniInputCell(value = uiState.contractEndDay, onValueChange = viewModel::updateContractEndDay, width = 30.dp)
                    Text("일", fontSize = 12.sp)
                }
            }

            // ── 안내 문구 ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF9C4))
                    .border(0.5.dp, Color(0xFFD1D1D1))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "※ 음료, 주류는 낱개반품가능 그외의 잡화는 개봉시 반품불가입니다.",
                    fontSize = 11.sp,
                    color = Color(0xFF5D4037)
                )
                Text("(인)", fontSize = 11.sp, color = Color(0xFF5D4037))
            }

            // ── 테이블 헤더 ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6))
                    .border(0.5.dp, Color(0xFFD1D1D1)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableHeaderCell("번호",   weight = 0.5f)
                TableHeaderCell("품명", weight = 2f)
                TableHeaderCell("단가",  weight = 1f)
                TableHeaderCell("셋팅수량", weight = 0.8f)
                TableHeaderCell("반품수량", weight = 0.8f)
                TableHeaderCell("금액",  weight = 1.2f)
            }

            // ── 품목 리스트 ───────────────────────────────────────────────────
            uiState.items.forEach { item ->
                FuneralItemRow(
                    item = item,
                    onReturnQtyChange = { qty -> viewModel.updateReturnQuantity(item.number, qty) }
                )
            }

            // ── 합계 / 서명 행 ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // 셋팅확인 서명 + 반품확인 서명
                Column(modifier = Modifier.weight(3f)) {
                    // 셋팅확인
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .border(0.5.dp, Color(0xFFD1D1D1))
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("셋팅확인", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        SignatureArea(
                            label = "셋팅확인",
                            modifier = Modifier.weight(1f).height(60.dp),
                        )
                    }
                    // 반품확인
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("반품확인", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        SignatureArea(
                            label = "반품확인",
                            modifier = Modifier.weight(1f).height(60.dp),
                        )
                    }
                }
                // 합계
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .border(0.5.dp, Color(0xFFD1D1D1)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("합  계", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatWon(uiState.totalAmount),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )
                }
            }

            // ── 반품 차감 / 최종 합계 ─────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A237E))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("셋팅 합계", color = Color(0xFFB0BEC5), fontSize = 13.sp)
                        Text(formatWon(uiState.settingAmount), color = Color.White, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("반품 차감", color = Color(0xFFFFCC80), fontSize = 13.sp)
                        Text("- ${formatWon(uiState.returnAmount)}", color = Color(0xFFFF8A65), fontSize = 13.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                    Text("최종 합계", color = Color.White, fontSize = 13.sp)
                    Text(
                        text = formatWon(uiState.totalAmount),
                        color = Color(0xFFFFD54F),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    // ── 셋팅확인 서명 다이얼로그 ─────────────────────────────────────────────
    if (showSettingSignDialog) {
        SignatureDialog(
            onDismiss = { showSettingSignDialog = false },
            onConfirm = { path ->
                settingSignature = Path().apply { addPath(path) }
                showSettingSignDialog = false
            }
        )
    }

    // ── 반품확인 서명 다이얼로그 ─────────────────────────────────────────────
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

// ─── 테이블 헤더 셀 ─────────────────────────────────────────────────────────── ───────────────────────────────────────────────────────────
@Composable
private fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF1A237E),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── 품목 행 ──────────────────────────────────────────────────────────────────
@Composable
private fun FuneralItemRow(
    item: FuneralItem,
    onReturnQtyChange: (Int) -> Unit
) {
    val isReturned = item.returnQuantity > 0
    val rowBg = if (isReturned) Color(0xFFFFF3E0) else Color.White

    var inputText by remember(item.returnQuantity) {
        mutableStateOf(if (item.returnQuantity == 0) "" else item.returnQuantity.toString())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(rowBg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 번호
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.number.toString(), fontSize = 11.sp, color = Color(0xFF757575))
        }
        // 품명
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFE0E0E0))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = item.name, fontSize = 12.sp)
        }
        // 단가
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFE0E0E0))
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(text = formatWon(item.unitPrice), fontSize = 11.sp, color = Color(0xFF424242))
        }
        // 셋팅수량 — 고정
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .background(Color(0xFFECEFF1))
                .border(0.5.dp, Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.settingQuantity.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )
        }
        // 반품수량 — 입력
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .border(0.5.dp, if (isReturned) Color(0xFFFF9800) else Color(0xFFE0E0E0)),
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
                .border(0.5.dp, Color(0xFFE0E0E0))
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = formatWon(item.amount),
                fontSize = 11.sp,
                color = if (isReturned) Color(0xFF388E3C) else Color(0xFF212121),
                fontWeight = if (isReturned) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

// ─── 유틸 ─────────────────────────────────────────────────────────────────────
private fun formatWon(amount: Long): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(amount) + "원"