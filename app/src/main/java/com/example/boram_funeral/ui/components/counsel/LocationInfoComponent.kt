package com.example.boram_funeral.ui.components.counsel

import androidx.compose.runtime.*
import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomTextField

@Composable
fun LocationInfoComponent(
    locationAdmission: String, // 유치장소
    locationCare: String,      // 요양장소
    funeralCompany: String,    // 상조회사
    onAdmissionChange: (String) -> Unit,
    onCareChange: (String) -> Unit,
    onCompanyChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row (){
            // 섹션 헤더
            Text(
                modifier = Modifier.width(120.dp),
                text = "장소 및 기관",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ),
            )
            Spacer(modifier = Modifier.width(58.dp))
            Row (modifier = Modifier.fillMaxWidth()){
                Box (modifier = Modifier.weight(1f)){
                    // 1. 유치장소 (1단)
                    CustomTextField(
                        label = "유치장소",
                        value = locationAdmission,
                        onValueChange = onAdmissionChange,
                        placeholder = "유치 장소를 입력하세요.",
                        height = 38.dp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box (modifier = Modifier.weight(1f)){
                    // 2. 요양장소 (1단)
                    CustomTextField(
                        label = "요양장소",
                        value = locationCare,
                        onValueChange = onCareChange,
                        placeholder = "요양 장소를 입력하세요.",
                        height = 38.dp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box (modifier = Modifier.weight(1f)){
                    // 3. 상조회사 (1단)
                    CustomTextField(
                        label = "상조회사",
                        value = funeralCompany,
                        onValueChange = onCompanyChange,
                        placeholder = "상조회사명을 입력하세요.",
                        height = 38.dp
                    )
                }
            }
        }
    }
}