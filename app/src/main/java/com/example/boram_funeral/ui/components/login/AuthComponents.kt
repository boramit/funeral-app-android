package com.example.boram_funeral.ui.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.theme.PretendardFamily
import com.example.boram_funeral.ui.theme.boram_Border_Color
import com.example.boram_funeral.ui.theme.boram_Border_focus_Color
import com.example.boram_funeral.ui.theme.boram_Br_Color

@Composable
fun LoginInputStyle(
    label: String,
    isPassword: Boolean = false,
    value: String,                    // 추가: 뷰모델의 값을 전달받음
    onValueChange: (String) -> Unit,  // 추가: 값이 바뀔 때 뷰모델로 보고함
) {

    OutlinedTextField(
        value = value,               // 👈 외부 값을 사용
        onValueChange = onValueChange, // 👈 외부 함수를 실행
        modifier = Modifier
            .fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        label = {
            Text(
                text = label,
                // 1. 폰트 사이즈 조절 (원하는 크기로 지정)
                fontSize = 12.sp,
                fontFamily = PretendardFamily
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = boram_Border_focus_Color,
            unfocusedBorderColor = boram_Border_Color,
        )
    )
}

@Composable
fun LoginButton(
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = boram_Br_Color),
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = boram_Br_Color, modifier = Modifier.size(24.dp))
        } else {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}