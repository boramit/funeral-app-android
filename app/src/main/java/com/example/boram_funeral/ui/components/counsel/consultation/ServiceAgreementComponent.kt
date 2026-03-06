package com.example.boram_funeral.ui.components.counsel.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServiceAgreementComponent(
    modifier: Modifier = Modifier,
    onAgreementConfirmed: (Boolean) -> Unit
) {
    var isAgreed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. 헤더 섹션
        Text(
            text = "장례식장 이용 계약서",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // 2. 약관 텍스트 영역 (Scrollable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = """
                        제 1 조 (목적)
                        본 계약은 보람상조(이하 "회사")가 제공하는 상조 서비스의 이용 조건 및 절차에 관한 사항을 규정함을 목적으로 합니다.
                        
                        제 2 조 (계약의 효력)
                        본 약관은 고객이 동의하고 서명함과 동시에 효력이 발생하며...
                        
                        제 3 조 (서비스의 내용)
                        회사는 계약된 패키지에 따라 장례 용품 및 인력을 제공하며...
                        (상세 내용 생략)
                    """.trimIndent(),
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            }
        }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, ){
            // 3. 이용계약서
            Button(
                onClick = { /* 계약 진행 */ },
                modifier = Modifier.width(200.dp).height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF333333)
                )
            ) {
                Text("계약서 작성하기", color = Color.White)
            }
        }
    }
}