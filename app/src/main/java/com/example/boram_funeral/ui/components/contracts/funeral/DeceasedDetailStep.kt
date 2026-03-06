package com.example.boram_funeral.ui.components.contracts.funeral

// 1. Compose 기본 UI 및 레이아웃 관련
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField

// 2. Compose 디자인 수치 및 스타일 관련
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 3. (중요) 앞서 만든 공통 부품 임포트 (패키지 경로에 맞춰 수정 필요)
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell

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

    StepLayout(stepNumber = 2, title = "제2ㄴ페이지 (장례식장 이용 계약서)") {}

    // 1. 각 필드의 상태를 remember로 관리 (테스트용)
    var deceasedName by remember { mutableStateOf("") }
    var bongwan by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var jumin by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "제2조 (이용자 인적사항)",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 테이블 본체 시작
        Column(modifier = Modifier.border(1.dp, Color.Black)) {

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
        }
    }

}