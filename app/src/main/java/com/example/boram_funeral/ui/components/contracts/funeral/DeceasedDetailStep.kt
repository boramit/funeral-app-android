package com.example.boram_funeral.ui.components.contracts.funeral

// 1. Compose 기본 UI 및 레이아웃 관련
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll

// 2. Compose 디자인 수치 및 스타일 관련
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.components.contracts.funeral.base.InlineInputCell

// 3. (중요) 앞서 만든 공통 부품 임포트 (패키지 경로에 맞춰 수정 필요)
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell
import java.time.LocalDate
import java.util.Calendar

// UseContractTable.kt
//@Composable
//fun UseContractTable(
//    uiState: UseContractUiState, // 이용계약서 전용 데이터
//    onUpdate: (UseContractUiState) -> Unit
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        // --- 제2조 섹션 ---
//        Text("제2조 (이용자 인적사항)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
//
//        Column(modifier = Modifier.border(1.dp, Color.Black)) {
//            // 성함/본관/연령/성별 라인
//            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
//                LabelCell("고인명")
//                InputCell(uiState.deceasedName, { onUpdate(uiState.copy(deceasedName = it)) }, weight = 1.5f)
//                LabelCell("본관")
//                InputCell(uiState.deceasedBongwan, { onUpdate(uiState.copy(deceasedBongwan = it)) }, weight = 1f)
//                LabelCell("연령")
//                InputCell(uiState.deceasedAge, { onUpdate(uiState.copy(deceasedAge = it)) }, weight = 0.8f)
//                LabelCell("성별")
//                InputCell(uiState.deceasedGender, { onUpdate(uiState.copy(deceasedGender = it)) }, weight = 0.8f)
//            }
//
//            // 주민번호/주소 라인
//            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
//                LabelCell("주민번호")
//                InputCell(uiState.deceasedJumin, { onUpdate(uiState.copy(deceasedJumin = it)) }, weight = 2f)
//                LabelCell("주소")
//                InputCell(uiState.deceasedAddress, { onUpdate(uiState.copy(deceasedAddress = it)) }, weight = 4f)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // --- 제3조 섹션 ---
//        Text("제3조 (장례식장 정보)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
//        // ... (같은 방식으로 조립)
//    }
//}

@Composable
fun UseContractTableInputTest() {

    val year = Calendar.getInstance().get(Calendar.YEAR)

//    StepLayout(stepNumber = 2, title = "제2페이지 (장례식장 이용 계약서)") {}

    // 1. 각 필드의 상태를 remember로 관리 (테스트용)
    var deceasedName by remember { mutableStateOf("") }
    var bongwan by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var jumin by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

        Column (modifier = Modifier.fillMaxWidth(0.8f), horizontalAlignment = Alignment.CenterHorizontally,){
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "장례식장 이용 계약서",
                style = TextStyle(
                    color = Color(0xFF05195F), // 로고와 맞춘 네이비 색상
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),){
                // 최상단 임포트 필요: androidx.compose.foundation.layout.FlowRow
                FlowRow(
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center, // 텍스트와 폼의 높이 정렬
                    maxItemsInEachRow = Int.MAX_VALUE // 한 줄에 최대한 많이 배치
                ) {
                    Text("보람 의정부장례식장을 사업자로 하고 ", style = TextStyle(fontSize = 16.sp))
                    // 텍스트 사이에 들어갈 입력 폼
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // 텍스트와 입력창의 높이를 일직선으로 맞춤
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "故",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "고인 성함",
                            width = 100.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "님",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                    Text(
                        text = "의",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // 텍스트 사이에 들어갈 입력 폼
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // 텍스트와 입력창의 높이를 일직선으로 맞춤
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = "상주",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "상주 성함",
                            width = 100.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "님을(를) 이용자로 하여",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("다음과 같이 장례식장 이용 계약을 체결한다.", style = TextStyle(fontSize = 16.sp))
            }
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)){
                Text(
                    text = "제1조 (계약기간)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 최상단 임포트 필요: androidx.compose.foundation.layout.FlowRow
                FlowRow(
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center, // 텍스트와 폼의 높이 정렬
                    maxItemsInEachRow = Int.MAX_VALUE // 한 줄에 최대한 많이 배치
                ) {
                    Text("계약기간은 ", style = TextStyle(fontSize = 16.sp))
                    // 텍스트 사이에 들어갈 입력 폼
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // 텍스트와 입력창의 높이를 일직선으로 맞춤
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${year}년",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "월",
                            width = 40.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = " 월",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "일",
                            width = 40.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = " 일",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "부터",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // 텍스트 사이에 들어갈 입력 폼
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // 텍스트와 입력창의 높이를 일직선으로 맞춤
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${year}년",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "월",
                            width = 40.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = " 월",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // 성함 입력 칸
                        InlineInputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            placeholder = "일",
                            width = 40.dp // 성함 길이에 맞춰 적절히 조절
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = " 일",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                    Text(
                        text = "까지 한다.",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD1D1D1)
            )
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)){
                Text(
                    text = "제2조(이용자 인적사항)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 행 1: 고인명, 본관, 연령, 성별
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("고인명")
                    InputCell(
                        value = deceasedName,
                        onValueChange = { deceasedName = it },
                        weight = 1.5f,
                        placeholder = "이름 입력"
                    )

                    LabelCell("본관")
                    InputCell(
                        value = bongwan,
                        onValueChange = { bongwan = it },
                        weight = 1.5f
                    )

                    LabelCell("연령")
                    InputCell(
                        value = age,
                        onValueChange = { age = it },
                        weight = 1f
                    )

                    LabelCell("성별")
                    InputCell(
                        value = gender,
                        onValueChange = { gender = it },
                        weight = 1f
                    )
                }
                // 행 2: 주민번호, 주소
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("주민번호")
                    InputCell(
                        value = jumin,
                        onValueChange = { jumin = it },
                        weight = 3f,
                        placeholder = "000000-0000000"
                    )

                    LabelCell("주소")
                    InputCell(
                        value = address,
                        onValueChange = { address = it },
                        weight = 5f
                    )
                }
                // 행 3: 종교, 세례명, 사망장소, 사망일시
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min), // 모든 자식의 높이를 가장 큰 요소에 맞춤
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. 종교 (가로형)
                    LabelCell("종교")
                    InputCell(
                        value =deceasedName , // 변수명 구분
                        onValueChange = { deceasedName = it },
                        weight = 0.5f,
                        placeholder = "종교 입력"
                    )

                    // 2. 직분 / 세례명 (가로형)
                    LabelCell("직분 / \n 세례명")
                    InputCell(
                        value = deceasedName,
                        onValueChange = { deceasedName = it },
                        weight = 0.5f,
                        border=null,
                        )

                    Column(modifier = Modifier.weight(1f)) {
                        LabelCell("주민번호") // ColumnScope 버전 호출 (가로를 꽉 채움)
                        InputCell(
                            value = jumin,
                            onValueChange = { jumin = it },
                            placeholder = "000000-0000000"
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        LabelCell("주민번호") // ColumnScope 버전 호출 (가로를 꽉 채움)
                        InputCell(
                            value = jumin,
                            onValueChange = { jumin = it },
                            placeholder = "000000-0000000"
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD1D1D1)
            )
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)){
                Text(
                    text = "제3조(장례식장에 관한 정보)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 행 1: 고인명, 본관, 연령, 성별
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("고인명")
                    InputCell(
                        value = deceasedName,
                        onValueChange = { deceasedName = it },
                        weight = 1.5f,
                        placeholder = "이름 입력"
                    )

                    LabelCell("본관")
                    InputCell(
                        value = bongwan,
                        onValueChange = { bongwan = it },
                        weight = 1.5f
                    )

                    LabelCell("연령")
                    InputCell(
                        value = age,
                        onValueChange = { age = it },
                        weight = 1f
                    )

                    LabelCell("성별")
                    InputCell(
                        value = gender,
                        onValueChange = { gender = it },
                        weight = 1f
                    )
                }
            }
        }
    }

}