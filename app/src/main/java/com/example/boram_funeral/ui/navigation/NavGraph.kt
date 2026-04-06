package com.example.boram_funeral.ui.navigation

import com.example.boram_funeral.ui.screens.counsel.logic.CounselingViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.ConsultationViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // by 사용을 위해 필수
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState // 현재 경로 인식용
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.boram_funeral.ui.components.common.Modal.ModalSheet
import com.example.boram_funeral.ui.components.layout.Sidebar.BoramSideBar
import com.example.boram_funeral.ui.screens.home.HomeScreen
import com.example.boram_funeral.ui.screens.auth.LoginScreen
import com.example.boram_funeral.ui.screens.member.MemberListScreen
import com.example.boram_funeral.ui.screens.member.MemberDetailScreen
import  com.example.boram_funeral.ui.screens.counsel.CounselScreen
import com.example.boram_funeral.ui.screens.contract.ContractScreen
import com.example.boram_funeral.ui.screens.contract.logic.ContractViewModel
import com.example.boram_funeral.ui.screens.counsel.logic.CounselViewModel


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    // 1. 현재 화면의 정보를 가져옵니다.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. 공통 레이아웃(상단바/하단바)을 보여주지 않을 화면들을 정의합니다.
    val hideLayoutScreens = listOf("auth")

    val showSideBar = currentRoute !in hideLayoutScreens

    val sharedCounselViewModel: CounselViewModel = viewModel()
    val sharedContractViewModel: ContractViewModel = viewModel()
    val isContractOpen by sharedContractViewModel.isContractOpen.collectAsState()

    var isDrawerOpen by remember { mutableStateOf(false) }

    PermanentNavigationDrawer(
        drawerContent = {
            if (showSideBar && isDrawerOpen) {
                BoramSideBar(
                    navController = navController,
                    onCloseDrawer = { isDrawerOpen = false }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showSideBar) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .drawBehind {
                                drawLine(
                                    color = Color(0xFFF1F3F4),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                    ) {
                        IconButton(
                            onClick = { isDrawerOpen = !isDrawerOpen },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                                contentDescription = if (isDrawerOpen) "사이드바 닫기" else "사이드바 열기"
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            ModalSheet(
                isOpen = isContractOpen,
                onDismiss = { sharedContractViewModel.closeContract() },
            ) {
                ContractScreen(
                    onDismiss = { sharedContractViewModel.closeContract() },
                    contractViewModel = sharedContractViewModel,
                )
            }

            NavHost(
                navController = navController,
                startDestination = "auth",
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                composable("auth") {
                    LoginScreen(
                        onNavigateToMain = {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    )
                }

                composable("home") { HomeScreen(navController = navController) }
                composable("counsel") {
                    val counselingViewModel: CounselingViewModel = viewModel()
                    val consultationViewModel: ConsultationViewModel = viewModel()
                    CounselScreen(
                        navController = navController,
                        onBackClick = { navController.popBackStack() },
                        counselViewModel = sharedCounselViewModel,
                        counselingViewModel = counselingViewModel,
                        contractViewModel = sharedContractViewModel,
                        consultationViewModel = consultationViewModel,
                    )
                }

                composable("member") {
                    MemberListScreen(
                        navController = navController,
                        counselViewModel = sharedCounselViewModel,
                        onBackClick = { navController.popBackStack() },
                    )
                }

                composable(
                    route = "member_details/{eventId}",
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                    MemberDetailScreen(
                        eventId = eventId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}