package com.example.boram_funeral.ui.components.member

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onSearchClick: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomTextField(
            modifier = Modifier.weight(1f),
            value = name,
            onValueChange = { name = it },
            label = "성함",
            placeholder = "고인명을 작성해주세요.",
            height = 48.dp
        )

        DatePickerField(
            modifier = Modifier.weight(1f),
            label = "시작일",
            value = startDate,
            onDateSelected = { startDate = it }
        )

        DatePickerField(
            modifier = Modifier.weight(1f),
            label = "종료일",
            value = endDate,
            onDateSelected = { endDate = it }
        )

        Box(modifier = Modifier.width(80.dp)) {
            CustomButton(
                text = "검색",
                fullWidth = true,
                size = ButtonSize.Medium,
                icon = Icons.Default.Search,
                fontSize = 13.sp,
                backgroundColor = boram_Br_Color,
                onClick = { onSearchClick(name, startDate, endDate) }
            )
        }
    }
}
