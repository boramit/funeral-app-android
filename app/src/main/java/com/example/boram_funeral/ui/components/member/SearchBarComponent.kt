package com.example.boram_funeral.ui.components.member

import androidx.compose.material3.*
import androidx.compose.runtime.*

// 아이콘 관련
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

// 레이아웃 및 상태 관리
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Button.ButtonSize
import com.example.boram_funeral.ui.components.common.Button.CustomButton
import com.example.boram_funeral.ui.components.common.Input.CustomTextField
import com.example.boram_funeral.ui.components.common.Input.DatePickerField
import com.example.boram_funeral.ui.theme.boram_Br_Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    modifier: Modifier = Modifier,
    onSearchClick: (String, String, String) -> Unit // 이름, 시작일, 종료일 전달
) {
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(modifier = Modifier.weight(0.5f)){
                // 1. 성함 입력 (가변 너비)
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "성함",
                    placeholder = "고인명을 작성해주세요.",
                )
            }

            Box(modifier = Modifier.weight(1f)){
                DatePickerField(
                    label = "시작일",
                    value = startDate,
                    onDateSelected = { startDate = it },
                )
            }

            Box(modifier = Modifier.weight(1f)){
                DatePickerField(
                    label = "종료일",
                    value = endDate,
                    onDateSelected = { endDate = it },
                )
            }

            Box(modifier = Modifier.width(80.dp)){
                CustomButton(
                    text = "검색",
                    size = ButtonSize.Medium,
                    icon = Icons.Default.Search,
                    fontSize = 13.sp,
                    backgroundColor = boram_Br_Color,
                    onClick = { onSearchClick(name, startDate, endDate) },
                )
            }
        }
    }
}