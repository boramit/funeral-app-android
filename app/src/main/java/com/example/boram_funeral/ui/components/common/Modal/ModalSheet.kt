package com.example.boram_funeral.ui.components.common.Modal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ModalSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    contentPadding: Dp = 24.dp,
    content: @Composable () -> Unit,

    ) {
    if (isOpen) {
        Dialog(

            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true, // 뒤로가기 버튼으로 닫기
                dismissOnClickOutside = true, // 외부 클릭 시 닫기
                usePlatformDefaultWidth = false
            )
        ) {
            // 다이얼로그의 카드 형태 디자인
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(8.dp), // 모서리 둥글게
                color = Color.White,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                ) {
                    // 주입될 내용 (제목, 설명, 버튼 등)
                    content()
                }
            }
        }
    }
}