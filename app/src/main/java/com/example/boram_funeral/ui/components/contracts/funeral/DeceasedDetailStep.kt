package com.example.boram_funeral.ui.components.contracts.funeral

// 1. Compose 기본 UI 및 레이아웃 관련
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import com.example.boram_funeral.ui.components.contracts.funeral.base.ContractFooter
import com.example.boram_funeral.ui.components.contracts.funeral.base.InlineInputCell

// 3. (중요) 앞서 만든 공통 부품 임포트 (패키지 경로에 맞춰 수정 필요)
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.MiniInputCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.RoomPriceRow
import com.example.boram_funeral.ui.components.contracts.funeral.base.ServiceInputRow
import com.example.boram_funeral.ui.components.contracts.funeral.base.SignatureDialog
import com.example.boram_funeral.ui.components.contracts.funeral.base.TotalServiceRow
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

    // remember를 사용해야 화면이 다시 그려져도 값이 유지됩니다.
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString()) }
    var month by remember { mutableStateOf((Calendar.getInstance().get(Calendar.MONTH) + 1).toString()) }
    var day by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()) }

    var isDialogVisible by remember { mutableStateOf(false) }
    var signatureData by remember { mutableStateOf<Path?>(null) }

    var signatureUpdateTick by remember { mutableStateOf(0) }

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
        .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
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
                            width = 40.dp
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // 좌우 패딩만 고정
            )
            {
                Text(
                    text = "제2조(이용자 인적사항)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    // 행 1: 고인명, 본관, 연령, 성별
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell("고인명")
                        InputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            weight = 2f,
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
                            weight = 1.5f
                        )
                    }
                    // 행 2: 주민번호, 주소
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell(
                            "주민\n번호",
                            modifier = Modifier.fillMaxHeight().heightIn(min = 60.dp)
                        )
                        InputCell(
                            value = jumin,
                            onValueChange = { jumin = it },
                            weight = 3f,
                            placeholder = "000000-0000000"
                        )

                        LabelCell("주소", modifier = Modifier.fillMaxHeight().heightIn(min = 60.dp))
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
                            value = deceasedName, // 변수명 구분
                            onValueChange = { deceasedName = it },
                            weight = 1f,
                            placeholder = "종교 입력",
                        )
                        // 2. 직분 / 세례명 (가로형)
                        LabelCell("직분 /\n 세례명")
                        InputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            weight = 1f,
                        )

                        Column(modifier = Modifier.weight(3f)) {
                            LabelCell(
                                "사망장소",
                                modifier = Modifier.heightIn(min = 14.dp)
                            ) // ColumnScope 버전 호출 (가로를 꽉 채움)
                            InputCell(
                                value = jumin,
                                onValueChange = { jumin = it },
                                placeholder = "000000-0000000"
                            )
                        }

                        Column(modifier = Modifier.weight(3f)) {
                            LabelCell(
                                "사망일시",
                                modifier = Modifier.heightIn(min = 14.dp)
                            ) // ColumnScope 버전 호출 (가로를 꽉 채움)
                            InputCell(
                                value = jumin,
                                onValueChange = { jumin = it },
                                placeholder = "000000-0000000"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    // 행 1: 임차인, 임차인 주민번호, 관계
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        LabelCell("임차인\n(계약자)")
                        InputCell(
                            value = deceasedName,
                            onValueChange = { deceasedName = it },
                            weight = 2f,
                            placeholder = "이름 입력"
                        )

                        LabelCell(
                            "주민등록\n번호",
                            modifier = Modifier.fillMaxHeight().heightIn(min = 60.dp)
                        )
                        InputCell(
                            value = jumin,
                            onValueChange = { jumin = it },
                            weight = 2f,
                            placeholder = "000000-0000000"
                        )

                        LabelCell("관계")
                        InputCell(
                            value = bongwan,
                            onValueChange = { bongwan = it },
                            weight = 2f
                        )
                    }
                    // 행 2: 주민번호, 주소
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Column(modifier = Modifier.weight(3f)) {
                            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                LabelCell("집전화")
                                InputCell(
                                    value = jumin,
                                    onValueChange = { jumin = it },
                                    weight = 2f,
                                    placeholder = "000000-0000000"
                                )
                            }
                            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                LabelCell("휴대폰")
                                InputCell(
                                    value = jumin,
                                    onValueChange = { jumin = it },
                                    weight = 2f,
                                    placeholder = "000000-0000000"
                                )
                            }
                        }
                        LabelCell("주소", modifier = Modifier.fillMaxHeight().heightIn(min = 60.dp))
                        InputCell(
                            value = address,
                            onValueChange = { address = it },
                            weight = 5f
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD1D1D1)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text(
                    text = "제3조(장례식장에 관한 정보)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 행 1: 고인명, 본관, 연령, 성별
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("안치실")
                            InputCell(
                                value = jumin,
                                onValueChange = { jumin = it },
                                weight = 2f,
                                placeholder = "000000-0000000"
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LabelCell("안치일시") // 왼쪽 회색 라벨
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .border(0.5.dp, Color(0xFFD1D1D1)),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(
                                                value = year,
                                                { year = it },
                                                width = 60.dp
                                            )
                                            Text("년")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(month, { month = it }, width = 40.dp)
                                            Text(text = "월", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(day, { day = it }, width = 40.dp)
                                            Text(text = "일", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "시", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "분", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("빈소")
                            InputCell(
                                value = jumin,
                                onValueChange = { jumin = it },
                                weight = 2f,
                                placeholder = "000000-0000000"
                            )
                        }
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("입관일시")
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .border(0.5.dp, Color(0xFFD1D1D1)),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(
                                                value = year,
                                                { year = it },
                                                width = 60.dp
                                            )
                                            Text("년")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(month, { month = it }, width = 40.dp)
                                            Text(text = "월", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(day, { day = it }, width = 40.dp)
                                            Text(text = "일", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "시", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "분", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("장지")
                            InputCell(
                                value = jumin,
                                onValueChange = { jumin = it },
                                weight = 2f,
                                placeholder = "000000-0000000"
                            )
                        }
                        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                            LabelCell("발인일시")
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .border(0.5.dp, Color(0xFFD1D1D1)),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(
                                                value = year,
                                                { year = it },
                                                width = 60.dp
                                            )
                                            Text("년")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(month, { month = it }, width = 40.dp)
                                            Text(text = "월", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(day, { day = it }, width = 40.dp)
                                            Text(text = "일", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp)) // 일/시 사이 간격

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "시", style = TextStyle(fontSize = 14.sp))
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            MiniInputCell(jumin, { jumin = it }, width = 40.dp)
                                            Text(text = "분", style = TextStyle(fontSize = 14.sp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("빈소입실 일시", style = TextStyle(fontSize = 16.sp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniInputCell(value = year, { year = it }, width = 60.dp)
                            Text("년", style = TextStyle(fontSize = 14.sp))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniInputCell(value = month, { year = it }, width = 40.dp)
                            Text("월", style = TextStyle(fontSize = 14.sp))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniInputCell(value = day, { year = it }, width = 40.dp)
                            Text("일", style = TextStyle(fontSize = 14.sp))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniInputCell(value = jumin, { jumin = it }, width = 40.dp)
                            Text("시", style = TextStyle(fontSize = 14.sp))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MiniInputCell(value = jumin, { jumin = it }, width = 40.dp)
                            Text("분", style = TextStyle(fontSize = 14.sp))
                        }
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFD1D1D1)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "제4조(장례식장 이용료)",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "※빈소임대료※", style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // 1. 헤더 영역
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        LabelCell("구 분", modifier = Modifier.weight(3f).fillMaxHeight())

                        // 요금 헤더 (상/하 분리)
                        Column(modifier = Modifier.weight(2f).fillMaxHeight()) {
                            LabelCell("요 금", modifier = Modifier.fillMaxWidth().weight(1f))
                            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                                LabelCell("1시간", modifier = Modifier.weight(1f).fillMaxHeight())
                                LabelCell("1일", modifier = Modifier.weight(1f).fillMaxHeight())
                            }
                        }

                        LabelCell("단 위", modifier = Modifier.weight(1f).fillMaxHeight())
                        LabelCell("금 액", modifier = Modifier.weight(1.5f).fillMaxHeight())
                    }
                    // 2. 데이터 영역 (빈소임대료 통합 칸 + 호실별 리스트)
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        // [좌측 통합 라벨] 모든 호실의 높이를 합친 만큼 늘어남
                        LabelCell(
                            text = "빈소임대료",
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )

                        // [우측 호실 리스트 기둥]
                        Column(modifier = Modifier.weight(6.5f)) {
                            // 호실 데이터 1 (예: 201호)
                            RoomPriceRow(
                                roomName = "201호실",
                                seatCount = "56석",
                                size = "50평형",
                                priceH = "26,000원",
                                priceD = "624,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                            // 호실 데이터 2 (예: 202호)
                            RoomPriceRow(
                                roomName = "202호실",
                                seatCount = "84석",
                                size = "65평형",
                                priceH = "41,000원",
                                priceD = "984,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                            // 호실 데이터 3 (예: 203호)
                            RoomPriceRow(
                                roomName = "203호실",
                                seatCount = "80석",
                                size = "65평형",
                                priceH = "41,000원",
                                priceD = "984,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                            // 호실 데이터 4 (예: VIP호실)
                            RoomPriceRow(
                                roomName = "VIP호실",
                                seatCount = "120석",
                                size = "118평형",
                                priceH = "50,000원",
                                priceD = "1,200,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                            // 호실 데이터 5 (예: VVIP호실)
                            RoomPriceRow(
                                roomName = "VVIP호실",
                                seatCount = "140석",
                                size = "136평형",
                                priceH = "60,000원",
                                priceD = "1,440,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                            // 호실 데이터 6 (예: VIP+VVIP호실)
                            RoomPriceRow(
                                roomName = "VIP+VVIP호실",
                                seatCount = "270석",
                                size = "270평형",
                                priceH = "100,000원",
                                priceD = "2,400,000원",
                                totalAmount = "",
                                onAmountChange = { "" }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "빈소임대료 및 안치료는 24시간을 1일, 24시간 미만 12시간 이상일 경우 1일 계산\n" +
                                    "12시간 미만일 경우 시간단위 산정, 1시간 미만은 1시간으로 산정한다.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                letterSpacing = (-0.5).sp
                            )
                        )
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "※부대시설 이용료※", style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // --- 헤더 영역 (비율 2 : 3 : 1 : 1.5) ---
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                            .background(Color(0xFFF5F5F5))
                    ) {
                        LabelCell("구 분", modifier = Modifier.weight(2f).fillMaxHeight())
                        LabelCell("요 금", modifier = Modifier.weight(3f).fillMaxHeight())
                        LabelCell("단 위", modifier = Modifier.weight(1f).fillMaxHeight())
                        LabelCell("금 액", modifier = Modifier.weight(1.5f).fillMaxHeight())
                    }

                    // --- 데이터 행 영역 ---
                    ServiceInputRow(title = "시신안치료", price = "1일  / 120,000원", unitText = "일")
                    ServiceInputRow(title = "수시초염료", price = "150,000원 / 1회", unitText = "회")
                    ServiceInputRow(title = "염습료", price = "300,000원 / 1회", unitText = "회")
                    ServiceInputRow(title = "시간 외/특수염습", price = "300,000원 / 1회", unitText = "회")
                    ServiceInputRow(title = "입관실사용료", price = "300,000원 / 1회", unitText = "회")
                    ServiceInputRow(title = "소독비", price = "100,000원 / 1회", unitText = "회")
                    ServiceInputRow(title = "감염성 폐기물(적출물)", price = "100,000원 / 1회", unitText = "회")
                    ServiceInputRow(
                        title = "오물수거료(1일)",
                        price = "VIP+VVIP 70,000원 / 일반실 50,000원",
                        unitText = "일"
                    )
                    TotalServiceRow(
                        countValue = "",
                        onCountChange = { "" },
                        unitText = "원",
                        totalAmount = "600,000"
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "본 계약과 관련한 세부 내용은 '보람장례식장 이용약관'에 기재되어 있으니 계약서 작성 전 장례식장 이용약관에 대한 \n" +
                                    "설명을 받으신 후 작성하시기 바라며, 계약서 작성이 끝나면 계약서, 약관을 교부 받으시기 바랍니다.",
                            style = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                letterSpacing = (-0.5).sp
                            )
                        )
                    }
                }
            }
            ContractFooter(
                // 여기에 현재 서명 상태를 전달해서 화면에 그리게 할 수 있습니다.
                capturedPath = signatureData,
                updateTick = signatureUpdateTick,
                onSignatureClick = { isDialogVisible = true } // 클릭 시 다이얼로그 켬
            )
        }
        // 3. 조건부 다이얼로그 표시
        if (isDialogVisible) {
            SignatureDialog(
                onDismiss = { isDialogVisible = false },
                onConfirm = { path ->
                    // 중요: 기존 path를 복사해서 새 객체로 전달해야 안전합니다.
                    signatureData = Path().apply { addPath(path) }
                    // 숫자를 바꿔서 리컴포지션을 강제합니다.
                    signatureUpdateTick++
                    isDialogVisible = false
                }
            )
        }
    }
}