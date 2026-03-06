package com.example.boram_funeral.ui.components.counsel

import androidx.compose.runtime.*
import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField
import com.example.boram_funeral.ui.components.common.Input.CustomTextField

@Composable
fun BasicInfoComponent(

    typeOptions: List<String>,
    relationshipOptions: List<String>,
    counselingNo: String,
    counselingType: String,
    customerName: String,
    phoneNumber: String,
    relationship: String,
    onTypeChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRelationshipChange: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(){
            // 섹션 타이틀
            Text(
                modifier = Modifier.width(120.dp),
                text = "기본 정보",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
            )
            Spacer(modifier = Modifier.width(58.dp))
            Column {

                Row(modifier = Modifier.fillMaxWidth()){
                    Box(modifier = Modifier.weight(1f)){
                        // 1. 상담번호 (중요 정보 - 1단)
                        CustomTextField(
                            label = "상담번호",
                            value = counselingNo,
                            onValueChange = {},
                            readOnly = true, // 상담번호는 보통 자동생성되므로 읽기전용
                            height = 38.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)){
                        // 2. 상담고객 (핵심 정보 - 1단 강조)
                        CustomTextField(
                            label = "상담고객",
                            value = customerName,
                            onValueChange = onNameChange,
                            placeholder = "고객명을 입력하세요.",
                            height = 38.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)){
                        // 3. 연락처 (데이터가 길 수 있어 1단 권장)
                        CustomTextField(
                            label = "연락처",
                            value = phoneNumber,
                            onValueChange = onPhoneChange,
                            placeholder = "010-0000-0000",
                            height = 38.dp
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                // 4. 상담유형 & 관계 (짧은 항목들 - 2단 배치)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomDropdownField(
                            label = "상담유형",
                            options = typeOptions,
                            selectedOption = counselingType,
                            onOptionSelected = onTypeChange,
                            placeholder = "선택",
                            height = 38.dp

                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CustomDropdownField(
                            label = "관계",
                            options = relationshipOptions,
                            selectedOption = relationship,
                            onOptionSelected = onRelationshipChange,
                            placeholder = "선택",
                            height = 38.dp
                        )
                    }
                }
            }
        }
    }
}