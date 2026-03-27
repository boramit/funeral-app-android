package com.example.boram_funeral.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.screens.contract.ContractScreen
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val contractViewModel: ContractViewModel = viewModel()
    val counselViewModel: CounselViewModel = viewModel()

    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(text = "메인")

        Button(onClick = { isDialogOpen = true }) {
            Text("모달 열기")
        }

        ModalSheet(
            isOpen = isDialogOpen,
            onDismiss = { isDialogOpen = false },
        ) {
            // ✅ ContractScreen이 모든 것을 담당
            ContractScreen(
                onDismiss = { isDialogOpen = false },
                contractViewModel = contractViewModel,
            )
        }
    }
}