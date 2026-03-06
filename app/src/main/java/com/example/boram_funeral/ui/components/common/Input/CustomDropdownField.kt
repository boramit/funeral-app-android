package com.example.boram_funeral.ui.components.common.Input

import com.example.boram_funeral.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    height: androidx.compose.ui.unit.Dp = 48.dp,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    placeholder: String = "선택해주세요.",
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null
) {
    var expanded by remember { mutableStateOf(false) }

    val rotateBy by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300), // 0.3초 동안 부드럽게 회전
        label = "IconRotation"
    )

    Box(modifier = modifier){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            CustomTextField(
                label = label,
                value = selectedOption,
                onValueChange = {},
                readOnly = true, // 드롭다운이므로 직접 입력 방지
                inputModifier = Modifier
                    .menuAnchor()
                    .height(height),
                fontSize = fontSize,
                placeholder = placeholder,
                shape = shape,
                border = border,
                trailingIcon = {
                    // 화살표 아이콘
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down), // 방금 만든 파일
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(rotateBy)
                    )
                }
            )

            // 실제 팝업 메뉴
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .graphicsLayer {
                        shadowElevation = 20f // 그림자 깊이
                        spotShadowColor = Color(0xFFD1D9E6) // 주 그림자 색상
                        ambientShadowColor = Color(0xFFD1D9E6).copy(alpha = 0.5f) // 주변광 그림자 색상
                        this.shape = RoundedCornerShape(8.dp) // 모양 일치
                        clip = false // 그림자 번짐 허용
                    }
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .exposedDropdownSize(),
                border = BorderStroke(1.dp, Color(0xFFF1F3F4)),
                offset = DpOffset(x = 0.dp, y = 4.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()           // 가로 꽉 채우기
                            .padding(horizontal = 8.dp), // 아이템 자체를 안쪽으로 밀어넣기
                        text = {
                            Text(text = option, fontSize = fontSize)
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
