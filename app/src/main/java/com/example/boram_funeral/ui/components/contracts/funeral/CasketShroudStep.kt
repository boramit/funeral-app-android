package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.TableHeaderCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.contract.model.FuneralServiceItem

@Composable
fun CasketShroudStep(viewModel: ContractViewModel) {

    // ViewModel 상태 구독 — remember 변수 제거
    val uiState by viewModel.uiState.collectAsState()

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            FuneralTable(
                leftItems  = uiState.leftItems,
                rightItems = uiState.rightItems,
                onLeftQuantityChange  = { name, qty -> viewModel.updateLeftItemQuantity(name, qty) },
                onRightQuantityChange = { name, qty -> viewModel.updateRightItemQuantity(name, qty) },
                onRightRemarksChange  = { name, rem -> viewModel.updateRightItemRemarks(name, rem) },
            )
        }
    }
}

@Composable
fun RowScope.TableCellItem(
    item: FuneralServiceItem?,
    onQuantityChange: (String) -> Unit = {},
) {
    if (item != null) {
        var showImageDialog by remember { mutableStateOf(false) }

        // 이미지 모달 (noModal이면 표시하지 않음)
        if (showImageDialog && !item.noModal) {
            Dialog(onDismissRequest = { showImageDialog = false }) {
                androidx.compose.material3.Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        if (item.imageResId != null) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = item.imageResId),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 400.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }

        // 품명
        Box(
            modifier = Modifier
                .weight(2.5f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFD1D1D1))
                .then(if (!item.noModal) Modifier.clickable { showImageDialog = true } else Modifier)
                .padding(8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = item.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (item.imageResId != null && !item.noModal) Color(0xFF1A56DB) else Color.Unspecified
            )
        }

        // 수량
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFD1D1D1))
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (item.isReadOnly) {
                Text(text = item.unit, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color.DarkGray)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = item.quantity,
                        onValueChange = onQuantityChange,  // ✅ ViewModel 연결
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 12.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    if (item.unit.isNotEmpty()) {
                        Text(text = item.unit, fontSize = 12.sp, modifier = Modifier.padding(start = 2.dp))
                    }
                }
            }
        }

        // 금액
        Column(
            modifier = Modifier
                .weight(2.3f)
                .fillMaxHeight()
                .border(0.5.dp, Color(0xFFD1D1D1))
        ) {
            val prices = item.price.split("/")
            prices.forEachIndexed { index, price ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .let {
                            if (index > 0) it.drawBehind {
                                drawLine(Color(0xFFD1D1D1), Offset(0f, 0f), Offset(size.width, 0f), 1f)
                            } else it
                        }
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = if (price.trim().isEmpty()) "" else "${price.trim()}원", fontSize = 12.sp)
                }
            }
        }
    } else {
        Box(modifier = Modifier.weight(6f).fillMaxHeight().border(0.5.dp, Color(0xFFD1D1D1)))
    }
}

@Composable
fun RowScope.FullWidthHeaderCell(item: FuneralServiceItem, totalWeight: Float) {
    val bgColor = if (item.isYellowHeader) Color.Yellow else Color(0xFFF5F5F5)
    Box(
        modifier = Modifier
            .weight(totalWeight)
            .fillMaxHeight()
            .background(bgColor)
            .border(0.5.dp, Color(0xFFD1D1D1)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.name, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun FuneralTable(
    leftItems: List<FuneralServiceItem>,
    rightItems: List<FuneralServiceItem>,
    onLeftQuantityChange: (name: String, qty: String) -> Unit,
    onRightQuantityChange: (name: String, qty: String) -> Unit,
    onRightRemarksChange: (name: String, rem: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 헤더
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5)), verticalAlignment = Alignment.CenterVertically) {
            TableHeaderCell("품명", weight = 2.5f)
            TableHeaderCell("수량", weight = 1.2f)
            TableHeaderCell("금액", weight = 2.3f)
            TableHeaderCell("품명", weight = 2.5f)
            TableHeaderCell("수량", weight = 1.2f)
            TableHeaderCell("금액", weight = 2.3f)
            TableHeaderCell("비고", weight = 1.5f)
        }

        var leftIdx = 0
        var rightIdx = 0

        while (rightIdx < rightItems.size) {
            val left  = leftItems.getOrNull(leftIdx)
            val right = rightItems.getOrNull(rightIdx)

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                TableCellItem(
                    item = left,
                    onQuantityChange = { qty -> left?.let { onLeftQuantityChange(it.name, qty) } }
                )
                leftIdx++

                if (right != null) {
                    if (right.isHeader || right.isYellowHeader) {
                        FullWidthHeaderCell(item = right, totalWeight = 7.5f)
                        rightIdx++
                    } else {
                        TableCellItem(
                            item = right,
                            onQuantityChange = { qty -> onRightQuantityChange(right.name, qty) }
                        )
                        rightIdx++
                        // 비고 칸
                        Box(
                            modifier = Modifier
                                .weight(1.5f)
                                .fillMaxHeight()
                                .border(0.5.dp, Color(0xFFD1D1D1))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextField(
                                value = right.remarks,
                                onValueChange = { onRightRemarksChange(right.name, it) },  // ✅ ViewModel 연결
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        }
                    }
                }
            }
        }

        // 남은 좌측 데이터
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Column(modifier = Modifier.weight(6.0f)) {
                while (leftIdx < leftItems.size) {
                    val leftItem = leftItems.getOrNull(leftIdx)
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        TableCellItem(
                            item = leftItem,
                            onQuantityChange = { qty -> leftItem?.let { onLeftQuantityChange(it.name, qty) } }
                        )
                    }
                    leftIdx++
                }
            }

            // 우측 안내 문구 + 서명란
            Column(
                modifier = Modifier.weight(7.5f).fillMaxHeight().border(0.5.dp, Color(0xFFD1D1D1)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "※ 임대차 계약 시 사용된 또는 예정된 물품을 기입한 것이며,\n 임차인의 요청에 의하여 추가되는 물품은\n 거래명세서에 기록하여 정산하므로 최종 정산 금액과 다를 수 있음",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(48.dp))
                Row(modifier = Modifier.width(320.dp).height(120.dp)) {
                    Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("상담자",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        SignatureArea(label = "상담자", modifier = Modifier.weight(1f).fillMaxWidth())
                    }
                    Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("확인자",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        SignatureArea(label = "확인자", modifier = Modifier.weight(1f).fillMaxWidth())
                    }
                }
            }
        }
    }
}