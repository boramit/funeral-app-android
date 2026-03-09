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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField

@Composable
fun RowScope.LabelCell(text: String, weight: Float = 1f) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .background(Color(0xFFF5F5F5)) // 이미지의 푸르스름한 회색
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ColumnScope.LabelCell(
    text: String,
    modifier: Modifier = Modifier // 유연성을 위해 modifier를 추가합니다.
) {
    Box(
        modifier = modifier
            .fillMaxWidth() // 세로 묶음이므로 가로를 꽉 채웁니다.
            .background(Color(0xFFF5F5F5))
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(vertical = 8.dp, horizontal = 12.dp), // 상하 여백 조절
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
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
            .height(48.dp)
            .focusable()
            .border(0.5.dp, Color(0xFFD1D1D1))
            .padding(horizontal = 12.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
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
fun ColumnScope.InputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth() // 세로 모드에서는 보통 가로를 꽉 채웁니다.
            .height(48.dp)
            .focusable()
            .border(0.5.dp, Color(0xFFD1D1D1)),
        textStyle = TextStyle(fontSize = 16.sp),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
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
fun InlineInputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    width: Dp = 100.dp
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(width),
        textStyle = TextStyle(
            fontSize = 12.sp, // 일반 Text 컴포넌트와 크기를 맞춤
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center // 문장 중간에서는 중앙 정렬이 예쁩니다.
        ),
        decorationBox = { innerTextField ->
            Column {
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(color = Color.Gray, fontSize = 12.sp),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
                // 밑줄만 살짝 주면 '입력란'임을 알 수 있어 UX가 좋아집니다.
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 2.dp)
                )
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