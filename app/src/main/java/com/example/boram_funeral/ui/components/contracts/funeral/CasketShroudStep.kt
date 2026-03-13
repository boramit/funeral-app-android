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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.contracts.funeral.base.FullWidthHeaderCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureArea
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureDialog
import com.example.boram_funeral.ui.components.contracts.funeral.base.TableCellItem
import com.example.boram_funeral.ui.components.contracts.funeral.base.TableHeaderCell

// 1. 데이터 모델 정의
data class FuneralServiceItem(
    val name: String,
    val unit: String = "",
    val price: String = "", // 금액을 콤마 포함 문자열로 처리하거나 Long으로 통일
    val isHeader: Boolean = false,
    val isYellowHeader: Boolean = false,
    val quantity: MutableState<String> = mutableStateOf(""), // 기본값 지정
    val isReadOnly: Boolean = false,
    val remarks: MutableState<String> = mutableStateOf("")
    )

@Composable
fun CasketShroudStep() {
    // 2. 샘플 데이터 구성 (이미지 내용 기반)
    val leftItems = remember {
        mutableStateListOf(
            FuneralServiceItem(name = "1.7 오동화장관", unit = "개", price = "400,000"),
            FuneralServiceItem(name = "2.5 오동맞춤관", unit = "개", price = "600,000"),
            FuneralServiceItem(name = "4.2 오동매장관", unit = "개", price = "480,000"),
            FuneralServiceItem(name = "4.2 솔송매장관", unit = "개", price = "800,000"),
            FuneralServiceItem(name = "4.2 향매장관", unit = "개", price = "1,800,000"),
            FuneralServiceItem(name = "횡대(오동,솔송,향)", unit = "조", price = ""),
            FuneralServiceItem(name = "수의", unit = "벌", price = ""),
            FuneralServiceItem(name = "멧베(명품/특명품)", unit = "기본 2필", price = "300,000 / 400,000", isReadOnly = true),
            FuneralServiceItem(name = "꼬깔(명품)", unit = "기본 1필", price = "200,000", isReadOnly = true),
            FuneralServiceItem(name = "칠성판", unit = "개", price = "70,000"),
            FuneralServiceItem(name = "수세포", unit = "개", price = "30,000"),
            FuneralServiceItem(name = "수세이불", unit = "개", price = "100,000"),
            FuneralServiceItem(name = "시신위생약품", unit = "SET", price = "150,000"),
            FuneralServiceItem(name = "베개", unit = "개", price = "10,000"),
            FuneralServiceItem(name = "배허리띠", unit = "SET", price = "20,000"),
            FuneralServiceItem(name = "액자리본", unit = "개", price = "10,000"),
            FuneralServiceItem(name = "혼백함", unit = "개", price = "10,000"),
            FuneralServiceItem(name = "위패/명패", unit = "개", price = "30,000"),
            FuneralServiceItem(name = "무연향", unit = "BOX", price = "20,000"),
            FuneralServiceItem(name = "고급 효원향", unit = "BOX", price = "25,000"),
            FuneralServiceItem(name = "양초", unit = "개", price = "5,000"),
            FuneralServiceItem(name = "부의록", unit = "개", price = "20,000"),
            FuneralServiceItem(name = "고급완장세트(두줄)", unit = "개", price = ""),
            FuneralServiceItem(name = "고급완장세트(한줄)", unit = "개", price = ""),
            FuneralServiceItem(name = "알코올", unit = "개", price = "20,000"),
            FuneralServiceItem(name = "탈지면", unit = "개", price = "40,000"),
            FuneralServiceItem(name = "한지", unit = "권", price = "40,000"),
            FuneralServiceItem(name = "입관속옷", unit = "인", price = "80,000"),
            FuneralServiceItem(name = "습신", unit = "개", price = "10,000"),
            FuneralServiceItem(name = "고급자수염보", unit = "개", price = "150,000"),
            FuneralServiceItem(name = "초석(보공)", unit = "인", price = "40,000"),
            FuneralServiceItem(name = "명정", unit = "개", price = "100,000"),
            FuneralServiceItem(name = "소창(결관포)", unit = "마", price = "40,000"),
            FuneralServiceItem(name = "다라니경(고급/일반)", unit = "장", price = "50,000 / 10,000")
        )
    }
    val rightItems = remember {
        mutableStateListOf(
            FuneralServiceItem("기독경/천주경", "장", "10,000"),
            FuneralServiceItem("윤아", "SET", "5,000"),
            FuneralServiceItem("예단", "SET", "10,000"),
            FuneralServiceItem("축문", "권", "5,000"),
            FuneralServiceItem("운구용 위생킷", "개", ""),

            // 함수 호출 대신 데이터 객체를 넣습니다.
            FuneralServiceItem(name = "유골함", isHeader = true),
            FuneralServiceItem("유골함", "개", ""),
            FuneralServiceItem("각인비", "회", ""),
            FuneralServiceItem("전사비", "회", ""),

            FuneralServiceItem(name = "영정사진", isHeader = true),
            FuneralServiceItem("인화", "회", "100,000"),
            FuneralServiceItem("액자", "회", "40,000"),
            FuneralServiceItem("빈소 LED 영정", "개", "60,000"),

            FuneralServiceItem(name = "장의차량", isHeader = true),
            FuneralServiceItem("리무진 , 버스", "", "1,500,000"),
            FuneralServiceItem("이송비", "", ""),

            FuneralServiceItem(name = "비아젬", isHeader = true), // 노란색 헤더 데이터
            FuneralServiceItem("비아젬", "", ""),
            FuneralServiceItem("오마주", "", ""),
            FuneralServiceItem("헤리티지박스", "", "")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Column (modifier = Modifier.fillMaxWidth(0.8f)){
                Text("장례서비스 이용 내역")
            }
        }
        item {
            Column (modifier = Modifier.fillMaxWidth(0.8f)){
                FuneralTable(leftItems, rightItems)
            }
        }
    }
}

@Composable
fun FuneralTable(
    leftItems: List<FuneralServiceItem>,
    rightItems: List<FuneralServiceItem>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {
        // --- 1. 상단 공통 헤더 ---
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5)),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        // --- 2. [대칭 구간] 비아젬(우측 데이터)이 있는 동안 ---
        while (rightIdx < rightItems.size) {
            val left = leftItems.getOrNull(leftIdx)
            val right = rightItems.getOrNull(rightIdx)

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                TableCellItem(left)
                leftIdx++

                if (right != null) {
                    if (right.isHeader || right.isYellowHeader) {
                        FullWidthHeaderCell(item = right, totalWeight = 7.5f)
                        rightIdx++
                    } else {
                        TableCellItem(right)
                        rightIdx++
                        Box(
                            modifier = Modifier.weight(1.5f).fillMaxHeight().border(0.5.dp, Color(0xFFD1D1D1)).padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextField(
                                value = right.remarks.value,
                                onValueChange = { right.remarks.value = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // 왼쪽: 남은 데이터들을 Column으로 묶음
            Column(modifier = Modifier.weight(6.0f)) {
                while (leftIdx < leftItems.size) {
                    val leftItem = leftItems.getOrNull(leftIdx)
                    // 별도 함수 없이 직접 Row와 TableCellItem을 작성
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        TableCellItem(leftItem)
                    }
                    leftIdx++
                }
            }


            // 오른쪽: 전체를 차지하는 하나의 큰 빈 박스
            Column (
                modifier = Modifier
                    .weight(7.5f)
                    .fillMaxHeight()
                    .border(0.5.dp, Color(0xFFD1D1D1)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "※ 임대차 계약 시 사용된 또는 예정된 물품을 기입한 것이며, 임차인의 요청에 의하여 추가되는 물품은\n" +
                            "거래명세서에 기록하여 정산하므로 최종 정산 금액과 다를 수 있음",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier
                        .width(320.dp) // 너비가 200dp면 두 칸을 담기에 너무 좁을 수 있어 늘렸습니다.
                        .height(120.dp) // 글자 + 서명란 공간을 위해 높이를 조금 더 확보
                ) {
                    // 1. 상담자 섹션
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally // 글자 중앙 정렬
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5))){
                            Text(
                                text = "상담자",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        // 서명 영역이 남은 공간을 다 차지하도록 weight(1f) 부여
                        SignatureArea(
                            label = "상담자",
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }

                    // 2. 확인자 섹션
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5))){
                            Text(
                                text = "확인자",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        SignatureArea(
                            label = "확인자",
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
