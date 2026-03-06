package com.example.boram_funeral.ui.components.contracts.funeral.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.runtime.*
import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField

@Composable
fun RowScope.LabelCell(text: String, weight: Float = 1f) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .background(Color(0xFFF1F3F6)) // 이미지의 푸르스름한 회색
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RowScope.InputCell(
    value: String,
    onValueChange: (String) -> Unit,
    weight: Float = 1f,
    placeholder: String = ""
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        // 필기 인식이 잘 되도록 포커스 및 터치 영역 최적화
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(0.5.dp, Color(0xFFD1D1D1))
            .focusable(), // 펜 포커스 허용
        textStyle = TextStyle(fontSize = 12.sp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = Color.LightGray, fontSize = 11.sp)
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun RowScope.SelectCell(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    weight: Float = 1f,
    placeholder: String = "선택"
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .border(0.5.dp, Color(0xFFD1D1D1)) // 테이블 테두리 유지
    ) {
        // 이미 만들어둔 커스텀 드롭다운 호출
        CustomDropdownField(
            label = "",
            shape = RectangleShape,
            border = null,
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            fontSize = 12.sp,
            placeholder = placeholder,
            modifier = Modifier.fillMaxSize()
        )
    }
}