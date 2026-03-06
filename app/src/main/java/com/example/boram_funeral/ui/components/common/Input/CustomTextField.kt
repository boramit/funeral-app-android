package com.example.boram_funeral.ui.components.common.Input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    label: String,
    enabled: Boolean = true,
    value: String = "",
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    minLines: Int = 1,
    readOnly: Boolean = false, // 추가: 읽기 전용 여부
    trailingIcon: @Composable (() -> Unit)? = null, // 추가: 아이콘 자리
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier, // 추가: BasicTextField 전용 모디파이어
    fontSize: TextUnit = 13.sp,
    height: Dp = Dp.Unspecified,
    width: Dp = Dp.Unspecified,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null
    ) {

    // 2. 상태에 따른 배경색 결정
    val backgroundColor = if (enabled) Color.White else Color(0xFFF5F5F5)

    val defaultBorderColor = if (enabled) Color(0xFFE8EAED) else Color(0xFFDDDDDD)
    val finalBorder = border ?: BorderStroke(1.dp, defaultBorderColor)

    Column(modifier = modifier.fillMaxWidth()) {

        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        BasicTextField(
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = fontSize, color = Color.Black),
            minLines = minLines,
            readOnly = readOnly, // 설정 적용
            modifier = inputModifier, // 중요: 여기에 menuAnchor가 들어감
            singleLine = minLines == 1,
            decorationBox = { innerTextField ->
                Row( // Box 대신 Row를 써서 아이콘 공간 확보
                    modifier = Modifier
                        .clip(shape)
                        .then(
                            if (width != Dp.Unspecified) Modifier.width(width)
                            else Modifier.fillMaxWidth()
                        )
                        .then(
                            if (height != Dp.Unspecified) {
                                Modifier.height(height) // 외부 지정 높이가 최우선
                            } else {
                                // minHeight 대신 min을 사용해야 합니다.
                                Modifier.heightIn(min = if (minLines > 1) 120.dp else 42.dp)
                            }
                        )
                        .background(backgroundColor)
                        .border(finalBorder, shape)
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = if (minLines > 1) Alignment.Top else Alignment.CenterVertically)
                {
                    Box(modifier = Modifier.weight(1f),
                        contentAlignment = if (minLines == 1) Alignment.CenterStart else Alignment.TopStart) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.padding(top = if (minLines > 1) 12.dp else 0.dp))
                        }
                        innerTextField()
                    }
                    // 아이콘이 있다면 띄워줌
                    trailingIcon?.invoke()
                }
            }
        )
    }
}