package com.example.boram_funeral.ui.components.counsel

import androidx.compose.runtime.*
import androidx.compose.material3.*

import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.boram_funeral.ui.components.common.Input.CustomTextField

@Composable
fun SaleAgreementComponent(
    saleStatus: String,         // "적용" 또는 "미적용"
    saleCategory: String,       // 할인구분
    companyName: String,        // 기업/단체명
    agreementDetail: String,    // 협약 내용
    onStatusChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onCompanyNameChange: (String) -> Unit,
    onDetailChange: (String) -> Unit
) {
    val isEditable = saleStatus == "적용" // 적용일 때만 편집 가능

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(){
            // 섹션 헤더
            Text(
                modifier = Modifier.width(120.dp),
                text = "할인 및 협약 정보",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ),
            )
            Spacer(modifier = Modifier.width(58.dp))
            Column {
                // 1. 할인여부 선택 (라디오 버튼 행)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                    ) {
                    listOf("적용", "미적용").forEach { text ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .selectable(
                                    selected = (saleStatus == text),
                                    onClick = { onStatusChange(text) }
                                )
                        ) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                                RadioButton(
                                    selected = (saleStatus == text),
                                    onClick = { onStatusChange(text) },
                                    modifier = Modifier.size(24.dp) // 버튼 자체의 크기를 명시적으로 지정
                                )
                            }
                            Text(
                                text = text,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // 할인구분 & 기업/단체명
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            label = "할인구분",
                            value = saleCategory,
                            onValueChange = onCategoryChange,
                            placeholder = "MOU/임직원 등",
                            height = 38.dp,
                            enabled = isEditable // 비활성화 설정
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            label = "기업/단체명",
                            value = companyName,
                            onValueChange = onCompanyNameChange,
                            placeholder = "회사명 입력",
                            height = 38.dp,
                            enabled = isEditable // 비활성화 설정
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 협약 내용 (textarea 대용)
                CustomTextField(
                    label = "협약 내용",
                    value = agreementDetail,
                    onValueChange = onDetailChange,
                    placeholder = "상세 협약 내용을 입력하세요.",
                    minLines = 3,
                    enabled = isEditable // 비활성화 설정
                )
            }
        }
    }
}