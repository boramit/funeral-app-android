package com.example.boram_funeral.ui.components.layout.Sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.boram_funeral.R
import com.example.boram_funeral.ui.theme.SideBarBg

@Composable
fun BoramSideBar(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    // ModalDrawerSheet는 사이드바의 기본 배경과 모양을 잡아줍니다.
    ModalDrawerSheet(
        modifier = Modifier.width(240.dp), // 사이드바 너비 설정
        drawerShape = RectangleShape,
        drawerContainerColor= Color(0xFF1E1F24),
        windowInsets = WindowInsets(0, 0, 0, 0)
        ) {

        Spacer(modifier = Modifier.height(28.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SideBarBg)
                .padding(horizontal = 24.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_boram_logo_w),
                contentDescription = "Boram Logo",
                modifier = Modifier
                    .width(100.dp)
                    .height(32.dp),
                contentScale = ContentScale.Fit // 비율에 맞춰 영역을 채움
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 1. 헤더 영역 (사용자 정보)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "관리자 님",
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "사번: 20240001",
                color = White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. 메뉴 리스트 영역
        DrawerMenuItem(
            icon = Icons.Default.Home,
            label = "메인",
            onClick = {
                navController.navigate("home")
            }
        )
        DrawerMenuItem(
            icon = Icons.Default.Notifications,
            label = "신규 상담(로컬 테스트)",
            onClick = {
                navController.navigate("counsel") // 경로가 있다면 연결
            }
        )
        DrawerMenuItem(
            icon = Icons.Default.Notifications,
            label = "행사 리스트",
            onClick = {
                navController.navigate("member") // 경로가 있다면 연결
            }
        )

        Spacer(modifier = Modifier.weight(1f)) // 하단 로그아웃 버튼을 밀어내기 위한 스페이서

        // 3. 하단 설정/로그아웃 영역
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        DrawerMenuItem(
            icon = Icons.Default.ExitToApp,
            label = "로그아웃",
            onClick = {
                navController.navigate("auth") {
                    popUpTo(0) // 모든 백스택 제거 후 로그인으로 이동
                }
                onCloseDrawer()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = White) },
        label = { Text(text = label, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = White,) },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            // 핵심: 모든 상태의 배경색을 투명하게 설정
            selectedContainerColor = Color.Transparent,
            unselectedContainerColor = Color.Transparent,
            // 호버나 눌렀을 때의 색상도 투명하게 강제 고정
            selectedIconColor = White,
            unselectedIconColor = White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // 1. 기본 48dp보다 낮은 값을 줘서 높이 자체를 줄임
            .padding(horizontal = 8.dp) // 2. 좌우 여백만 살짝 유지
    )
}