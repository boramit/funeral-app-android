package com.example.boram_funeral.ui.navigation

import CounselingViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // by 사용을 위해 필수
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState // 현재 경로 인식용
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.boram_funeral.ui.components.layout.Sidebar.BoramSideBar
import com.example.boram_funeral.ui.screens.home.HomeScreen
import com.example.boram_funeral.ui.screens.auth.LoginScreen
import com.example.boram_funeral.ui.screens.member.MemberListScreen
import com.example.boram_funeral.ui.screens.member.MemberDetailScreen
import  com.example.boram_funeral.ui.screens.counsel.CounselScreen
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

    PermanentNavigationDrawer(
        drawerContent = {
            // 미리 만드신 BoramSideBar 컴포넌트 호출
            if (showSideBar) {
                BoramSideBar(
                    navController = navController,
                    onCloseDrawer = {}
                )
            }
        }
    ) {
        // 2. 기존 Scaffold 구조
        Scaffold{ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "auth",
                modifier = Modifier.padding(innerPadding)
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

                    CounselScreen(
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    counselViewModel = sharedCounselViewModel,
                    counselingViewModel = counselingViewModel
                )
                }

                // 2. 멤버 리스트 페이지
                composable("member") {
                    MemberListScreen(
                        navController = navController,
                        counselViewModel = sharedCounselViewModel,
                        onBackClick = { navController.popBackStack() },
                    )
                }

                // 3. (옵션) 기존 상세 페이지가 따로 필요하다면 유지
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