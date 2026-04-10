package com.example.boram_funeral.ui.components.contracts.funeral

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.boram_funeral.ui.screens.contract.pdf.LocalPageIndex
import com.example.boram_funeral.ui.screens.contract.pdf.LocalScrollStateRegistrar
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.TableHeaderCell
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.contract.model.FoodCategoryItem
import com.example.boram_funeral.ui.screens.contract.model.FoodItemOption
import com.example.boram_funeral.ui.screens.contract.model.FoodServiceItem

// 모든 행에 공통 적용되는 고정 높이 — 이 값으로 비고 열과 본문 행의 픽셀 높이를 일치시킴
private val ROW_HEIGHT = 36.dp

@Composable
fun FoodCateringStep(viewModel: ContractViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val pageIndex = LocalPageIndex.current
    val registerScrollState = LocalScrollStateRegistrar.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { registerScrollState(pageIndex, scrollState) }

    val foodItems   = uiState.foodItems
    val serviceItems = uiState.foodCategoryItems

    // 합계: 모든 품목 가격(콤마 제거 후 Long 파싱) 합산
    val totalPrice = foodItems.sumOf { it.price.replace(",", "").toLongOrNull() ?: 0L }
    val totalFormatted = "%,d".format(totalPrice)

    var selectedFoodItem by remember { mutableStateOf<FoodServiceItem?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(id = uiState.selectedFuneralHome.logoResId),
                contentDescription = "Logo",
                modifier = Modifier.width(250.dp).height(50.dp),
                contentScale = ContentScale.Fit
            )
            Text("주문번호 : 900", fontSize = 14.sp, color = Color(0xFF05195F))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            FoodTable(
                items = foodItems,
                total = totalFormatted,
                onItemClick = { selectedFoodItem = it },
            )
            SignatureTable(
                items = serviceItems,
                onLevelSelect = { category, level -> viewModel.updateFoodCategoryLevel(category, level) },
            )
        }
    }

    // 옵션 선택 모달
    selectedFoodItem?.let { target ->
        FoodOptionModal(
            target = target,
            onSelect = { option ->
                viewModel.updateFoodItemPrice(target, option)
                selectedFoodItem = null
            },
            onDismiss = { selectedFoodItem = null },
        )
    }
}

// ────────────────────────────────────────────────────────────────
// FoodTable
// ────────────────────────────────────────────────────────────────
@Composable
fun FoodTable(
    items: List<FoodServiceItem>,
    total: String,
    onItemClick: (FoodServiceItem) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F7FA)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeaderCell("구분",   1f)
            TableHeaderCell("품명",   2f)
            TableHeaderCell("원산지", 2.5f)
            TableHeaderCell("단위",   1.2f)
            TableHeaderCell("가격",   2.3f)
            TableHeaderCell("비고",   2f)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(9f)) {
                var skipCount = 0
                items.forEachIndexed { _, item ->
                    if (skipCount > 0) {
                        skipCount--
                        return@forEachIndexed
                    }

                    when (item.category) {
                        "국" -> {
                            val soupItems = items.filter { it.category == "국" }
                            FoodMergedRow(
                                category = "국",
                                unit = "50인분",
                                subItems = soupItems,
                                prices = listOf(soupItems[0].price, soupItems[2].price),
                                onItemClick = onItemClick,
                            )
                            skipCount = soupItems.size - 1
                        }
                        "반찬류" -> {
                            val sideItems = items.filter { it.category == "반찬류" }
                            FoodSideItemRow(
                                category = "반찬류",
                                sideItems = sideItems,
                                onItemClick = onItemClick,
                            )
                            skipCount = sideItems.size - 1
                        }
                        else -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(ROW_HEIGHT)
                                    .clickable { onItemClick(item) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TableCellText(item.category ?: "", 1f)
                                TableCellText(item.name, 2f, textAlign = TextAlign.Start)
                                TableCellText(item.origin, 2.5f)
                                TableCellText(item.unit, 1.2f)
                                Row(
                                    modifier = Modifier
                                        .weight(2.3f)
                                        .fillMaxHeight()
                                        .border(0.25.dp, Color(0xFFD1D1D1))
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("₩", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(item.price, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            TableRemarksColumn(total = total)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ────────────────────────────────────────────────────────────────
// 공용 셀: RowScope 안에서 weight 비율로 배치
// fontSize 11sp + maxLines 1 + Ellipsis → 좁은 열에서 텍스트 클리핑 방지
// ────────────────────────────────────────────────────────────────
@Composable
fun RowScope.TableCellText(
    text: String,
    weight: Float,
    backgroundColor: Color = Color.Transparent,
    textAlign: TextAlign = TextAlign.Center
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(0.25.dp, Color(0xFFD1D1D1))
            .background(backgroundColor)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = if (textAlign == TextAlign.Start) Alignment.CenterStart else Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(lineHeight = 14.sp)
        )
    }
}

// ────────────────────────────────────────────────────────────────
// 국 카테고리 병합 행
// 구분/단위 셀은 Box로 세로 병합, 품명/원산지는 각 행 ROW_HEIGHT 고정
// ────────────────────────────────────────────────────────────────
@Composable
fun FoodMergedRow(
    category: String,
    unit: String,
    subItems: List<FoodServiceItem>,
    prices: List<String>,
    onItemClick: (FoodServiceItem) -> Unit,
) {
    val rowCount = subItems.size
    val totalHeight = ROW_HEIGHT * rowCount
    val rowsPerPrice = if (prices.isNotEmpty()) rowCount / prices.size else rowCount

    Row(modifier = Modifier.fillMaxWidth()) {
        // 구분 — 전체 높이 병합
        Box(
            modifier = Modifier
                .weight(1f)
                .height(totalHeight)
                .border(0.25.dp, Color(0xFFD1D1D1))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(category, fontSize = 14.sp, textAlign = TextAlign.Center, style = TextStyle(lineHeight = 14.sp))
        }

        // 품명 / 원산지 — 행마다 ROW_HEIGHT 고정, 클릭 가능
        Column(modifier = Modifier.weight(4.5f)) {
            subItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ROW_HEIGHT)
                        .clickable { onItemClick(item) }
                ) {
                    TableCellText(item.name, 2f, textAlign = TextAlign.Start)
                    TableCellText(item.origin, 2.5f)
                }
            }
        }

        // 단위 — 전체 높이 병합
        Box(
            modifier = Modifier
                .weight(1.2f)
                .height(totalHeight)
                .border(0.25.dp, Color(0xFFD1D1D1))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(unit, fontSize = 14.sp, textAlign = TextAlign.Center, style = TextStyle(lineHeight = 14.sp))
        }

        // 가격 — 2개 가격이 각각 2행씩 담당
        Column(modifier = Modifier.weight(2.3f)) {
            prices.forEach { price ->
                Row(
                    modifier = Modifier
                        .height(ROW_HEIGHT * rowsPerPrice)
                        .fillMaxWidth()
                        .border(0.25.dp, Color(0xFFD1D1D1))
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("₩", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(price, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────
// 반찬류 병합 행
// 카테고리 셀은 전체 행 수만큼 세로 병합, 나머지는 행마다 ROW_HEIGHT 고정
// ────────────────────────────────────────────────────────────────
@Composable
fun FoodSideItemRow(
    category: String,
    sideItems: List<FoodServiceItem>,
    onItemClick: (FoodServiceItem) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // 카테고리 — 세로 병합
        Box(
            modifier = Modifier
                .weight(1f)
                .height(ROW_HEIGHT * sideItems.size)
                .border(0.25.dp, Color(0xFFD1D1D1))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(category, fontSize = 14.sp, textAlign = TextAlign.Center, style = TextStyle(lineHeight = 14.sp))
        }

        // 품명 / 원산지 / 단위 / 가격 (weight 합계 = 8f)
        Column(modifier = Modifier.weight(8f)) {
            sideItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ROW_HEIGHT)
                        .clickable { onItemClick(item) }
                ) {
                    TableCellText(item.name, 2f, textAlign = TextAlign.Start)
                    TableCellText(item.origin, 2.5f)
                    TableCellText(item.unit, 1.2f)
                    Row(
                        modifier = Modifier
                            .weight(2.3f)
                            .fillMaxHeight()
                            .border(0.25.dp, Color(0xFFD1D1D1))
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("₩", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(item.price, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────
// 비고 열
// 기존 weight 기반 → ROW_HEIGHT 고정 높이 기반으로 전환
// 상단 11행 = 밥(1)+국(4)+무침(1)+찜(1)+강정(1)+모듬전(1)+고추멸치볶음(1)+수육(1)
// 하단 5행  = 매실장아찌·콩나물무침·김치·샐러드·새우젓
// ────────────────────────────────────────────────────────────────
@Composable
fun RowScope.TableRemarksColumn(total: String) {
    Column(modifier = Modifier.weight(2f)) {
        // 상단 통합 박스
        Box(
            modifier = Modifier
                .height(ROW_HEIGHT * 11)
                .fillMaxWidth()
                .border(0.25.dp, Color(0xFFD1D1D1)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "음식 주문 후\n완성까지\n조리시간이\n1~2시간정도\n소요됩니다.",
                    fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("합계", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("₩ $total 원", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(
                    "※ 음식 주문 ※\n마감시간\n20시까지",
                    fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "수육,코다리찜\n명태강정,모듬전은\n19시 30분까지",
                    fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 16.sp
                )
            }
        }

        // 하단 5행 — 각각 ROW_HEIGHT 고정
        // 매실장아찌
        Row(modifier = Modifier.height(ROW_HEIGHT).fillMaxWidth()) {
            TableCellText("과일", 1f, Color(0xFFE1E9F5))
            TableCellText("주문", 1f)
        }
        // 콩나물무침
        Box(
            modifier = Modifier
                .height(ROW_HEIGHT)
                .fillMaxWidth()
                .border(0.25.dp, Color(0xFFD1D1D1))
                .background(Color(0xFFE1E9F5)),
            contentAlignment = Alignment.Center
        ) {
            Text("시가 적용", color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        // 김치
        Row(modifier = Modifier.height(ROW_HEIGHT).fillMaxWidth()) {
            TableCellText("방울토마토", 1f, Color(0xFFE1E9F5))
            TableCellText("", 1f)
        }
        // 샐러드
        Row(modifier = Modifier.height(ROW_HEIGHT).fillMaxWidth()) {
            TableCellText("귤", 1f, Color(0xFFE1E9F5))
            TableCellText("", 1f)
        }
        // 새우젓
        Row(modifier = Modifier.height(ROW_HEIGHT).fillMaxWidth()) {
            TableCellText("", 1f, Color(0xFFE1E9F5))
            TableCellText("", 1f)
        }
    }
}

// ────────────────────────────────────────────────────────────────
// SignatureTable
// ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureTable(
    items: List<FoodCategoryItem>,
    onLevelSelect: (category: String, level: String?) -> Unit
) {
    var modalCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 헤더
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F7FA))) {
            TableHeaderCell("종류", 1.5f)
            TableHeaderCell("가격", 4.5f)
            TableHeaderCell("금액", 1.5f)
            TableHeaderCell("확인", 2f)
        }

        // 본문
        // IntrinsicSize.Min + 중첩 weight() 조합은 Compose에서 intrinsic height 계산이
        // 정의되지 않아 Row가 첫 행 높이(~40dp)로 붕괴됨 → 명시적 높이로 대체
        val signatureColumnHeight = 40.dp * (items.size + 1)  // 서비스 항목 수 + 합계 행

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(7.5f)) {
                items.forEach { item ->
                    ServiceDataRow(
                        item = item,
                        onCategoryClick = { modalCategory = item.category },
                        onLevelSelect = { level -> onLevelSelect(item.category, level) },
                    )
                }
                // 합계 행
                Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                    TableCellText("합계", 1.5f, backgroundColor = Color(0xFFE1E2F1))
                    TableCellText("", 6f)
                }
            }

            // 서명 패드 — fillMaxHeight 대신 명시적 높이로 left Column과 높이 일치
            Column(
                modifier = Modifier.weight(2f).height(signatureColumnHeight)
                    .border(0.25.dp, Color(0xFFD1D1D1))
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = Alignment.Center) {
                        Text("상담자", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    SignatureArea(label = "상담자", modifier = Modifier.weight(1f).fillMaxWidth())
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = Alignment.Center) {
                        Text("확인자", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    SignatureArea(label = "확인자", modifier = Modifier.weight(1f).fillMaxWidth())
                }
            }
        }
    }

    // 이미지 모달
    if (modalCategory != null) {
        val category = modalCategory!!
        val item = items.find { it.category == category }
        BasicAlertDialog(
            onDismissRequest = { modalCategory = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = category, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD1D1D1), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("이미지 준비 중", fontSize = 14.sp, color = Color.Gray)
                        if (item != null && item.priceLevels.isNotEmpty()) {
                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                item.priceLevels.forEach { pd ->
                                    Text("${pd.level}  ${pd.price}원", fontSize = 13.sp, color = Color(0xFF444444))
                                }
                            }
                        } else if (item?.flatPrice != null) {
                            Text(item.flatPrice, fontSize = 13.sp, color = Color(0xFF444444))
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clickable { modalCategory = null }
                        .background(Color(0xFF05195F), RoundedCornerShape(8.dp))
                        .padding(horizontal = 32.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("닫기", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────
// ServiceDataRow
// ────────────────────────────────────────────────────────────────
@Composable
fun ServiceDataRow(
    item: FoodCategoryItem,
    onCategoryClick: () -> Unit,
    onLevelSelect: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(0.25.dp, Color(0xFFD1D1D1))
    ) {
        // 종류
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .border(0.25.dp, Color(0xFFD1D1D1))
                .clickable { onCategoryClick() }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.category,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                style = TextStyle(lineHeight = 14.sp)
            )
        }

        // 가격
        Row(modifier = Modifier.weight(4.5f).fillMaxHeight()) {87
            if (item.priceLevels.isNotEmpty()) {
                item.priceLevels.forEach { priceDetail ->
                    val isSelected = item.selectedLevel == priceDetail.level
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .background(if (isSelected) Color(0xFF05195F) else Color.Transparent)
                            .border(0.25.dp, Color(0xFFD1D1D1))
                            .clickable {
                                val newLevel = if (item.selectedLevel == priceDetail.level) null else priceDetail.level
                                onLevelSelect(newLevel)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = priceDetail.level,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            style = TextStyle(lineHeight = 18.sp)
                        )
                    }
                    TableCellText(priceDetail.price, 1.75f, textAlign = TextAlign.End)
                }
            } else {
                // flatPrice: 가격 열 전체(4.5f)를 채워야 함 — 기존 1f 오류 수정
                TableCellText(item.flatPrice ?: "", 4.5f)
            }
        }

        // 금액
        TableCellText(item.totalAmount, 1.5f)
    }
}

// ────────────────────────────────────────────────────────────────
// FoodOptionModal — 품목 클릭 시 열리는 옵션 선택 모달
// 현재는 동일 카테고리 항목을 임시 옵션으로 표시
// 추후 API 연동 후 options 리스트를 서버 응답으로 교체
// ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodOptionModal(
    target: FoodServiceItem,
    onSelect: (FoodItemOption) -> Unit,
    onDismiss: () -> Unit,
) {
    // 현재 적용된 단위와 일치하는 옵션을 초기 선택값으로
    val initialOption = target.options.find { it.unit == target.unit }
        ?: target.options.firstOrNull()
        ?: FoodItemOption(unit = target.unit, price = target.price)

    var selected by remember(target) { mutableStateOf(initialOption) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 타이틀 — 품명 표시
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = target.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF05195F),
                )
                if (target.origin.isNotEmpty()) {
                    Text(text = "원산지: ${target.origin}", fontSize = 11.sp, color = Color.Gray)
                }
            }

            // 단위별 가격 옵션 목록
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                target.options.forEach { option ->
                    val isSelected = option == selected
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) Color(0xFFE8ECFA) else Color(0xFFF5F7FA),
                                RoundedCornerShape(8.dp),
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                color = if (isSelected) Color(0xFF05195F) else Color(0xFFD1D1D1),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .clickable { selected = option }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = option.unit,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF05195F) else Color.Black,
                        )
                        Text(
                            text = "₩ ${option.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF05195F) else Color(0xFF444444),
                        )
                    }
                }
            }

            // API 연동 안내
            Text(
                text = "* 추후 API 연동 후 실제 옵션이 제공됩니다.",
                fontSize = 11.sp,
                color = Color.Gray,
            )

            // 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFD1D1D1), RoundedCornerShape(8.dp))
                        .clickable { onDismiss() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("취소", fontSize = 14.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF05195F), RoundedCornerShape(8.dp))
                        .clickable { onSelect(selected) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("선택", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
