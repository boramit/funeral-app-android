package com.example.boram_funeral.ui.components.contracts.funeral.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── 날짜/시간 입력 블록 공통 컴포넌트 ─────────────────────────────────────────
// 안치일시/입관일시/발인일시에서 반복되는 년/월/일/시/분 입력 구조를 컴포넌트로 분리
@Composable
fun RowScope.DateTimeInputBlock(
    year: String,
    month: String, onMonthChange: (String) -> Unit,
    day: String, onDayChange: (String) -> Unit,
    hour: String, onHourChange: (String) -> Unit,
    minute: String, onMinuteChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.weight(2f).border(0.5.dp, Color(0xFFD1D1D1)),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MiniInputCell(value = year, onValueChange = {}, width = 40.dp) // 년도는 읽기전용
                Text("년")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniInputCell(value = month, onValueChange = onMonthChange, width = 40.dp)
                    Text("월", style = TextStyle(fontSize = 14.sp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniInputCell(value = day, onValueChange = onDayChange, width = 40.dp)
                    Text("일", style = TextStyle(fontSize = 14.sp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniInputCell(value = hour, onValueChange = onHourChange, width = 40.dp)
                    Text("시", style = TextStyle(fontSize = 14.sp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniInputCell(value = minute, onValueChange = onMinuteChange, width = 40.dp)
                    Text("분", style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}