package com.example.boram_funeral.ui.components.common.Input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    placeholder: String = "선택해주세요."
) {
    var showModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // HTML 스타일 레이아웃 (Label 아래 Input 박스)
    Column(modifier = Modifier) {
        // 1. 라벨 (Label)
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333), // 진한 회색
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // 2. 커스텀 입력 박스 (Input Box)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp) // 입력창 높이 고정
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFE8EAED), RoundedCornerShape(8.dp))
                .clickable { showModal = true } // 박스 전체 클릭 시 달력 오픈
                .padding(horizontal = 16.dp, vertical = 2.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 선택된 날짜 텍스트
                Text(
                    text = if (value.isEmpty() || value == "날짜 선택") placeholder else value,
                    color = if (value.isEmpty() || value == "날짜 선택") Color.LightGray else Color.Black,
                    fontSize = 14.sp
                )

                // 달력 아이콘
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // 3. 달력 다이얼로그 (기존 로직 유지)
    if (showModal) {
        DatePickerDialog(
            onDismissRequest = { showModal = false },
            tonalElevation = 0.dp,
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ),
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    } ?: value
                    onDateSelected(selectedDate)
                    showModal = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showModal = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ))
        }
    }
}