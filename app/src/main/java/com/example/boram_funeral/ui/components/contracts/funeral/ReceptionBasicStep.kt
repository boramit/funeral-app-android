package com.example.boram_funeral.ui.components.contracts.funeral

// 1. Compose 기본 UI 및 레이아웃 관련
import android.R.attr.maxHeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Column (){
            Text(text = "logo")
            Spacer(modifier = Modifier.height(20.dp))
            Column (modifier = Modifier.border(1.dp, Color.Black)){
                // 1행: 유입경로 / 빈소
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("유입경로")
                    InputCell(value = inflowPath, onValueChange = { inflowPath = it }, weight = 2f, placeholder = "경로 입력")
                    LabelCell("빈소")
                    SelectCell(
                        selectedOption = roomName,             // value 대신 selectedOption
                        onOptionSelected = { roomName = it },   // onValueChange 대신 onOptionSelected
                        options = listOf("특실", "1호실", "2호실"), // 옵션 리스트 추가 필요
                        weight = 2f,
                        placeholder = "빈소 선택"
                    )
                }
                // 2행: 상주명 / 고인명
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("상주명")
                    InputCell(value = chiefMourner, onValueChange = { chiefMourner = it }, weight = 2f)
                    LabelCell("고인명")
                    InputCell(value = deceasedName, onValueChange = { deceasedName = it }, weight = 2f)
                }
                // 3행: 장례지도사
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    LabelCell("지도사명")
                    InputCell(value = directorName, onValueChange = { directorName = it }, weight = 5f)
                }
            }
        }
    }
}

