package com.example.boram_funeral.ui.screens.auth

import com.example.boram_funeral.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boram_funeral.ui.components.login.LoginButton
import com.example.boram_funeral.ui.components.login.LoginInputStyle
import com.example.boram_funeral.ui.screens.auth.logic.LoginViewModel
import com.example.boram_funeral.ui.theme.PretendardFamily
import com.example.boram_funeral.ui.theme.boram_Br_Color
import com.example.boram_funeral.ui.theme.boram_Text_lightColor

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(), // 뷰모델 주입
    onNavigateToMain: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(id = R.drawable.ic_boram_logo),
                contentDescription = "Boram Logo",
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp),
                contentScale = ContentScale.Fit // 비율에 맞춰 영역을 채움
            )
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Column(
                        modifier = Modifier.padding(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        // 자식 요소들 사이에 일괄적으로 16dp 간격을 부여합니다.
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ){
                        Text(
                            text = "반갑습니다.",
                            fontWeight = FontWeight.W400,
                            fontFamily = PretendardFamily,
                            fontSize = 18.sp,
                        )
                        Text(
                            text = "보람가족여러분.",
                            fontWeight = FontWeight.W700,
                            fontFamily = PretendardFamily,
                            fontSize = 28.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // 16dp만큼 간격 추가

                    Column(
                        modifier = Modifier.padding(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        // 자식 요소들 사이에 일괄적으로 16dp 간격을 부여합니다.
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                    // 1. 사원번호 입력
                        LoginInputStyle(
                            label = "사원번호",
                            value = viewModel.idText, // ViewModel 상태 연결
                            onValueChange = { viewModel.idText = it }
                        )

                        // 2. 비밀번호 입력
                        LoginInputStyle(
                            label = "비밀번호",
                            isPassword = true,
                            value = viewModel.pwText, // ViewModel 상태 연결
                            onValueChange = { viewModel.pwText = it }
                        )

                        // 3. 아이디 저장 체크박스 (간격 조절을 위해 별도 추가)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* 아이디 저장 체크 로직 */ },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = false, // 나중에 viewModel.isRememberId 등으로 연결
                                onCheckedChange = { /* 체크 처리 */ },
                                colors = CheckboxDefaults.colors(checkedColor = boram_Br_Color)
                            )
                            Text(text = "아이디 저장", fontSize = 14.sp, color = Color.Gray)
                        }                    }
                    Spacer(modifier = Modifier.height(16.dp)) // 16dp만큼 간격 추가
                    // 컴포넌트 3: 로그인 버튼
                    LoginButton(
                        text = "로그인",
                        onClick = {
                            // 로직 실행을 요청함
                            viewModel.performLogin(onSuccess = onNavigateToMain)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // 16dp만큼 간격 추가
                    Text(
                        text="사번을 모르시는 경우 보람 그룹웨어 마이페이지 또는 \n 영업관리팀에 문의 바랍니다.",
                        fontWeight = FontWeight.W400,
                        fontFamily = PretendardFamily,
                        lineHeight = 20.sp,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = boram_Text_lightColor
                    )
                }
            }
        }
    }
}