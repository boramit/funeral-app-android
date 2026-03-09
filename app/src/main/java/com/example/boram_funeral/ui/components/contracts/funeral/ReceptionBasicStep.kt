package com.example.boram_funeral.ui.components.contracts.funeral

// 1. Compose 기본 UI 및 레이아웃 관련
import android.R.attr.maxHeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
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
import com.example.boram_funeral.ui.components.contracts.funeral.base.InputCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.LabelCell
import com.example.boram_funeral.ui.components.contracts.funeral.base.SelectCell

@Composable
fun ReceptionBasicStep() {
//    StepLayout(stepNumber = 1, title = "제1페이지") {}

    var inflowPath by remember { mutableStateOf("") }
    var roomName by remember { mutableStateOf("") }
    var chiefMourner by remember { mutableStateOf("") }
    var deceasedName by remember { mutableStateOf("") }
    var directorName by remember { mutableStateOf("") }

    var eventType by remember {mutableStateOf("")}
    var death_reason by remember {mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,){
            Spacer(modifier = Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_funeral_logo_uijeongbu),
                contentDescription = "Boram Logo",
                modifier = Modifier
                    .width(300.dp)
                    .height(58.dp),
                contentScale = ContentScale.Fit // 비율에 맞춰 영역을 채움
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "장례식장 이용계약서",
                style = TextStyle(
                    color = Color(0xFF05195F), // 로고와 맞춘 네이비 색상
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Column (modifier = Modifier.fillMaxWidth(0.50f).padding(bottom = 300.dp),horizontalAlignment = Alignment.CenterHorizontally,){
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Box(modifier = Modifier.weight(1f)){
                    Row (modifier = Modifier.height(IntrinsicSize.Min)){
                        LabelCell("부고사유")
                        SelectCell(
                            selectedOption = death_reason,             // value 대신 selectedOption
                            onOptionSelected = { death_reason = it },   // onValueChange 대신 onOptionSelected
                            options = listOf("병사", "외인사", "자연사", "미상", "기타", "코로나"), // 옵션 리스트 추가 필요
                            weight = 3f,
                            placeholder = "선택"
                        )
                    }
                }
                Box (modifier = Modifier.weight(1f)){
                    Row (modifier = Modifier.height(IntrinsicSize.Min)){
                        LabelCell("행사형태")
                        SelectCell(
                            selectedOption = eventType,             // value 대신 selectedOption
                            onOptionSelected = { eventType = it },   // onValueChange 대신 onOptionSelected
                            options = listOf("자체 행사", "타상조 행사", "보람그룹 행사","무빈소 행사", "대관 행사"), // 옵션 리스트 추가 필요
                            weight = 3f,
                            placeholder = "선택"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 1행: 유입경로 / 빈소
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                LabelCell("유입경로")
                InputCell(value = inflowPath, onValueChange = { inflowPath = it }, weight = 3f, placeholder = "경로 입력")
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 2행: 상주명 / 고인명
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                LabelCell("빈소")
                SelectCell(
                    selectedOption = roomName,             // value 대신 selectedOption
                    onOptionSelected = { roomName = it },   // onValueChange 대신 onOptionSelected
                    options = listOf("특실", "1호실", "2호실"), // 옵션 리스트 추가 필요
                    weight = 3f,
                    placeholder = "빈소 선택"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 3행: 장례지도사
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                LabelCell("상주명")
                InputCell(value = chiefMourner, onValueChange = { chiefMourner = it }, weight = 2f)
                LabelCell("고인명")
                InputCell(value = deceasedName, onValueChange = { deceasedName = it }, weight = 2f)
                LabelCell("지도사명")
                InputCell(value = directorName, onValueChange = { directorName = it }, weight = 2f)
            }

        }
    }
}

