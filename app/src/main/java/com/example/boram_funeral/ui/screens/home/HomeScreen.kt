package com.example.boram_funeral.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.components.contracts.funeral.UseContractStepContent
import com.example.boram_funeral.ui.components.contracts.funeral.UseContractTableInputTest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(text = "메인");

        Button(onClick = { isDialogOpen = true }) {
            Text("모달 열기")
        }

        ModalSheet(
            isOpen = isDialogOpen,
            onDismiss = { isDialogOpen = false }
        ) {
            IconButton(onClick = { isDialogOpen = false }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기"
                )
            }
            Scaffold { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    // 위에서 만든 레이아웃 테스트용 컴포넌트 호출
                    UseContractStepContent(
                        onClose = { isDialogOpen = false },
                        onFinish = {
                            // PDF 저장 로직 실행 후 닫기
                            isDialogOpen = false
                        }
                    )                }
            }
        }
    }
}