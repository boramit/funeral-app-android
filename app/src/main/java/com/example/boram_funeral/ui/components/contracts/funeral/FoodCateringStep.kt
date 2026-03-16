package com.example.boram_funeral.ui.components.contracts.funeral

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.TableHeaderCell

// 1. 데이터 모델 정의
data class FoodServiceItem(
    val category: String? = null,     // 구분 (국, 반찬류 등)
    val name: String,                // 품명
    val origin: String = "",         // 원산지
    val unit: String,                // 단위
    val price: String,               // 가격
    val isHeader: Boolean = false,
    val isYellowHeader: Boolean = false,
    val isReadOnly: Boolean = false,
    val remarks: MutableState<String> = mutableStateOf("") // 비고란
)

data class ServiceItem(
    val category: String,       // 종류 (예: "초배상", "상 식", "노 제")
    val priceLevels: List<PriceDetail> = emptyList(), // 상, 중 등 가격 리스트
    val flatPrice: String? = null, // 단일 가격이나 수식이 필요한 경우 (예: "50,000 x ( )회")
    val totalAmount: String = "",  // 금액 칸 내용
    val isHeaderStyle: Boolean = false // 합계 행처럼 강조가 필요한 경우
)

data class PriceDetail(
    val level: String,  // "上", "中"
    val price: String   // "400,000"
)

@Composable
fun FoodCateringStep() {
    val FoodItems = remember {
        mutableStateListOf(
            FoodServiceItem(category = "밥", name = "밥(국내산)", unit = "50인분(5kg)", price = "60,000", origin = "쌀(국내산)"),
            FoodServiceItem(category = "국", name = "얼갈이된장국", unit = "50인분", price = "175,000", origin = "얼갈이(국내산)"),
            FoodServiceItem(category = "국", name = "북어국", unit = "50인분", price = "175,000", origin = "북어(러시아)"),
            FoodServiceItem(category = "국", name = "소고기 무국", unit = "50인분", price = "190,000", origin = "소고기(호주산)"),
            FoodServiceItem(category = "국", name = "육개장", unit = "50인분", price = "190,000", origin = "소고기(호주), 고추가루,고사리(중국)"),
            FoodServiceItem(category = "무침", name = "가오리 무침", unit = "(3kg)", price = "150,000", origin = "가오리(남미)"),
            FoodServiceItem(category = "찜", name = "코다리찜", unit = "(4kg)", price = "110,000", origin = "북어(러시아)"),
            FoodServiceItem(category = "강정", name = "명태 강정", unit = "(2kg)", price = "130,000", origin = "북어(러시아)"),
            FoodServiceItem(category = "반찬류", name = "모듬전 (동태전,부추전,해물전)", unit = "(4kg)", price = "130,000", origin = "동태(러시아), 야채(국내산,수입산), 해물(수입산)"),
            FoodServiceItem(category = "반찬류", name = "고추멸치볶음", unit = "(2kg)", price = "100,000", origin = "가이리멸치(국내산)"),
            FoodServiceItem(category = "반찬류", name = "수육", unit = "(8kg)(40인분)", price = "360,000", origin = "돼지고기(국내산)"),
            FoodServiceItem(category = "반찬류", name = "매실장아찌", unit = "(2kg)", price = "80,000", origin = "매실(국내산)"),
            FoodServiceItem(category = "반찬류", name = "콩나물무침", unit = "(3kg)", price = "50,000", origin = "콩(수입산)"),
            FoodServiceItem(category = "반찬류", name = "김치", unit = "(5kg)", price = "60,000", origin = "배추(국내산), 고추가루(국내산)"),
            FoodServiceItem(category = "반찬류", name = "샐러드", unit = "(3kg)", price = "80,000", origin = "사과(국내산)"),
            FoodServiceItem(category = "반찬류", name = "새우젓", unit = "(1kg)", price = "20,000", origin = "새우(중국산)"),
            )
    }
    val serviceItems = listOf(
        ServiceItem(
            category = "초배상",
            priceLevels = listOf(PriceDetail("上", "400,000"), PriceDetail("中", "300,000"))
        ),
        ServiceItem(
            category = "성복제",
            priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
        ),
        ServiceItem(
            category = "발인제",
            priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
        ),
        ServiceItem(
            category = "상 식",
            flatPrice = "50,000 x (      )회"
        ),
        ServiceItem(
            category = "노 제",
            priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
        ),
        ServiceItem(
            category = "위령제,산신제",
            flatPrice = "기본 200,000" // 이미지 하단 '기본'과 '200,000' 병합 구조 대응
        )
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 로고 및 주문번호 섹션 (기존 코드 유지)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_funeral_logo_uijeongbu),
                    contentDescription = "Logo",
                    modifier = Modifier.width(250.dp).height(50.dp),
                    contentScale = ContentScale.Fit
                )
                Text("주문번호 : 900", fontSize = 14.sp, color = Color(0xFF05195F))
            }
        }

        // 테이블 섹션
        item {
            Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                FoodTable(FoodItems)
                SignatureTable(serviceItems)
            }
        }
    }
}

@Composable
fun FoodTable(items: List<FoodServiceItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // --- 1. 테이블 헤더 (기존 동일) ---
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F7FA)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeaderCell("구분", 1f)
            TableHeaderCell("품명", 2f)
            TableHeaderCell("원산지", 2.5f)
            TableHeaderCell("단위", 1.2f)
            TableHeaderCell("가격", 2.3f)
            TableHeaderCell("비고", 2f)
        }

        // --- 2. 테이블 내용 ---
        // '국' 섹션처럼 여러 행을 묶어야 하는 경우를 위해 이터레이터를 조절하거나
        // 여기서는 이해하기 쉽게 '국' 카테고리가 시작될 때만 병합 행을 그리고 나머지는 패스하는 방식으로 예시를 듭니다.
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Column(modifier = Modifier.weight(9f)) {
                var skipCount = 0 // 병합 처리된 행을 건너뛰기 위한 변수

                items.forEachIndexed { index, item ->
                    if (skipCount > 0) {
                        skipCount--
                        return@forEachIndexed
                    }

                    // [병합 조건] 카테고리가 '국'이고 다음에 데이터가 더 있는 경우
                    if (item.category == "국") {
                        // '국' 섹션에 해당하는 데이터들을 추출 (예: 현재부터 다음 4개)
                        val soupItems = items.filter { it.category == "국" }

                        // 병합된 행 그리기
                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                            FoodMergedRow(
                                category = "국",
                                unit = "50인분",
                                // 4개의 품명과 원산지 리스트
                                subItems = soupItems.map { it.name to it.origin },
                                // ⚠️ 핵심: 가격은 4개가 아니라, 묶음 단위인 2개만 전달
                                // 첫 번째 가격(얼갈이+북어), 세 번째 가격(소고기+육개장)
                                prices = listOf(soupItems[0].price, soupItems[2].price)
                            )
                        }
                        skipCount = soupItems.size - 1 // 처리한 만큼 다음 루프 건너뜀
                    } else if (item.category == "반찬류") {
                        // 반찬류에 해당하는 모든 아이템을 한꺼번에 가져옴
                        val sideItems = items.filter { it.category == "반찬류" }

                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                            FoodSideItemRow(
                                category = "반찬류",
                                sideItems = sideItems
                            )
                        }
                        // 필터링된 개수만큼 다음 루프 건너뛰기
                        skipCount = sideItems.size - 1
                    } else {
                        // --- 일반 행 (기존 코드) ---
                        Row(
                            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCellText(item.category ?: "", 1f)
                            TableCellText(item.name, 2f, textAlign = TextAlign.Start)
                            TableCellText(item.origin, 2.5f)
                            TableCellText(item.unit, 1.2f)

                            // 가격 셀
                            Row(
                                modifier = Modifier.weight(2.3f).fillMaxHeight().border(0.25.dp, Color(0xFFD1D1D1)).padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₩", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                                Text(item.price, fontSize = 11.sp, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                            }

                        }
                    }
                }
            }
            TableRemarksColumn(
                upperTotalWeight = 11f, // 밥부터 수육까지 총 11줄 높이
                sideItemsCount = 5      // 매실장아찌부터 새우젓까지 5줄
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Composable
fun RowScope.TableCellText(text: String, weight: Float, backgroundColor: Color = Color.Transparent, textAlign: TextAlign = TextAlign.Center) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(0.25.dp, Color(0xFFD1D1D1))
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = if (textAlign == TextAlign.Start) Alignment.CenterStart else Alignment.Center
    ) {
        Text(text = text, fontSize = 11.sp, textAlign = textAlign, style = TextStyle(lineHeight = 18.sp))
    }
}
@Composable
fun RowScope.FoodMergedRow(
    category: String,
    unit: String,
    subItems: List<Pair<String, String>>,
    prices: List<String>
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [1] 구분 병합
        TableCellText(category, 1f)

        // [2] 품명/원산지 묶음
        Column(modifier = Modifier.weight(4.5f)) {
            subItems.forEach { (name, origin) ->
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    TableCellText(name, 2.0f / 4.5f * 4.5f, textAlign = TextAlign.Start) // 비율 유지
                    TableCellText(origin, 2.5f / 4.5f * 4.5f)
                }
            }
        }

        // [3] 단위 병합
        TableCellText(unit, 1.2f)

        // [4] 가격 병합 (2개 가격)
        Column(modifier = Modifier.weight(2.3f).fillMaxHeight()) {
            prices.forEach { price ->
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth().border(0.25.dp, Color(0xFFD1D1D1)).padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("₩", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                    Text(price, fontSize = 11.sp, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                }
            }
        }
    }
}

@Composable
fun RowScope.FoodSideItemRow(
    category: String,
    sideItems: List<FoodServiceItem>
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [1] 카테고리 (분류명만 세로 병합)
        TableCellText(text = category, weight = 1f)

        // [2] 나머지 정보들 (품명, 원산지, 단위, 가격을 묶어서 Column으로 배치)
        Column(modifier = Modifier.weight(8f)) { // 2.5(품명) + 2.0(원산지) + 1.2(단위) + 2.3(가격)
            sideItems.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    // 품명
                    TableCellText(text = item.name, weight = 2f, textAlign = TextAlign.Start)
                    // 원산지
                    TableCellText(text = item.origin, weight = 2.5f)
                    // 단위 (각 행마다 개별 표시)
                    TableCellText(text = item.unit, weight = 1.2f)
                    // 가격 (각 행마다 개별 표시)
                    Row(
                        modifier = Modifier
                            .weight(2.3f)
                            .fillMaxHeight()
                            .border(0.25.dp, Color(0xFFD1D1D1))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("₩", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                        Text(item.price, fontSize = 11.sp, fontWeight = FontWeight.Bold, style = TextStyle(lineHeight = 18.sp))
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableRemarksColumn(
    upperTotalWeight: Float, // 밥~수육까지의 총 줄 수 (11f 예상)
    sideItemsCount: Int      // 반찬류 행 개수 (5개 예상)
) {
    Column(modifier = Modifier.weight(2f).fillMaxHeight()) {
        // --- [1] 상단 영역: 밥부터 수육까지 통으로 병합 ---
        Box(
            modifier = Modifier
                .weight(upperTotalWeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("음식 주문 후\n완성까지\n조리시간이\n1~2시간정도\n소요됩니다.", fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(15.dp))
                Text("합계", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("₩ 1,540,000 원", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("※ 음식 주문 ※\n마감시간\n20시까지", fontSize = 10.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Text("수육,코다리찜\n명태강정,모듬전은\n19시 30분까지", fontSize = 10.sp, textAlign = TextAlign.Center)
            }
        }

        // --- [2] 하단 영역: 반찬류 5행에 각각 대응 ---
        for (index in 0 until sideItemsCount) {
            Row(
                modifier = Modifier
                    .weight(1f) // 왼쪽 반찬 한 행의 높이와 1:1 대응
                    .fillMaxWidth()
            ) {
                when (index) {
                    0 -> { // 매실장아찌 옆
                        TableCellText("과일", 1f, Color(0xFFE1E9F5))
                        TableCellText("주문", 1f)
                    }
                    1 -> { // 콩나물무침 옆
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color(0xFFE1E9F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("시가 적용", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    2 -> { // 김치 옆
                        TableCellText("방울토마토", 1f, Color(0xFFE1E9F5))
                        TableCellText("", 1f)
                    }
                    3 -> { // 샐러드 옆
                        TableCellText("귤", 1f, Color(0xFFE1E9F5))
                        TableCellText("", 1f)
                    }
                    4 -> { // 새우젓 옆
                        TableCellText("", 1f, Color(0xFFE1E9F5))
                        TableCellText("", 1f)
                    }
                }
            }
        }
    }
}


@Composable
fun SignatureTable(items: List<ServiceItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // [1] 헤더 영역 (종류, 가격, 금액, 확인)
        Row(modifier = Modifier.fillMaxWidth()) {
            TableHeaderCell("종 류", 1.5f)
            TableHeaderCell("가 격", 4.5f)
            TableHeaderCell("금 액", 1.5f)
            TableHeaderCell("확 인", 2f)
        }

        // [2] 본문 영역 (데이터 + 서명패드)
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

            // 왼쪽: 종류/가격/금액 리스트 영역
            Column(modifier = Modifier.weight(7.5f)) {
                items.forEach { item ->
                    ServiceDataRow(item) // 각 행 데이터 (초배상, 성복제 등)
                }

                // 마지막 합계 행
                Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                    TableCellText("합 계", 1.5f, backgroundColor = Color(0xFFE1E2F1))
                    TableCellText("", 6f) // 금액까지 쭉 비움
                }
            }

            // 오른쪽: 서명 패드 영역 (전체 높이 병합)
            Column(modifier = Modifier.weight(2f).fillMaxHeight().border(0.25.dp, Color.Black)) {

                // 1. 상담자 섹션 (제공해주신 코드 적용)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5)).padding(vertical = 4.dp), contentAlignment = Alignment.Center){
                        Text(text = "상담자", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    // 서명 영역 (실제 드로잉 패드가 들어갈 곳)
                    SignatureArea(label = "상담자", modifier = Modifier.weight(1f).fillMaxWidth())
                }


                // 2. 확인자 섹션 (제공해주신 코드 적용)
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5)).padding(vertical = 4.dp), contentAlignment = Alignment.Center){
                        Text(text = "확인자", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    // 서명 영역
                    SignatureArea(label = "확인자", modifier = Modifier.weight(1f).fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun ServiceDataRow(item: ServiceItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp) // 행 높이 고정 또는 가변
            .border(0.25.dp, Color.Black)
    ) {
        // [1] 종류 (종류 컬럼은 보라색 배경)
        TableCellText(item.category, 1.5f, backgroundColor = Color(0xFFD1D1D1))

        // [2] 가격 영역 (민트색 배경)
        Row(modifier = Modifier.weight(4.5f).fillMaxHeight().background(Color(0xFFD1D1D1))) {
            if (item.priceLevels.isNotEmpty()) {
                // 상, 중으로 나뉘는 경우
                item.priceLevels.forEach { priceDetail ->
                    TableCellText(priceDetail.level, 0.5f) // "上"
                    TableCellText(priceDetail.price, 1.75f, textAlign = TextAlign.End) // "400,000"
                }
            } else {
                // 상식처럼 단일 문구인 경우
                TableCellText(item.flatPrice ?: "", 1f)
            }
        }

        // [3] 금액 영역
        TableCellText(item.totalAmount, 1.5f)
    }
}