package com.example.boram_funeral.ui.components.common.Button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ButtonSize(val height: Dp, val fontSize: TextUnit) {
    Small(height = 36.dp, fontSize = 12.sp),
    Medium(height = 42.dp, fontSize = 14.sp),
    Large(height = 64.dp, fontSize = 16.sp)
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    size: ButtonSize = ButtonSize.Medium, // 기본 사이즈 설정
    fontSize: TextUnit? = null, // 폰트
    fullWidth: Boolean = true,  // 가로를 꽉 채울지 여부
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    backgroundColor: Color = Color(0xFF263446), // 보람 브랜드 컬러 (남색)
    contentColor: Color = Color.White,         // 글자 및 아이콘 색상
    enabled: Boolean = true,                    // 버튼 활성화 여부
    horizontalPadding: Dp = 16.dp,
) {


    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .height(size.height),
        enabled = enabled, // 클릭 가능 여부 주입
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = Color.LightGray, // 비활성화 시 배경색
            disabledContentColor = Color.Gray         // 비활성화 시 글자색
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // 내용물 중앙 정렬
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp) // 아이콘 크기 고정
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontSize = fontSize ?: size.fontSize,
                letterSpacing = (-0.5).sp
            )
        }
    }
}
