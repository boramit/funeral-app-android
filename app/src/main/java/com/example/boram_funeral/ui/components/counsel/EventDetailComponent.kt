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
import com.example.boram_funeral.ui.components.common.Input.CustomDropdownField
import com.example.boram_funeral.ui.components.common.Input.CustomTextField
import com.example.boram_funeral.ui.screens.counsel.model.FuneralHomeOption

@Composable
fun EventDetailComponent(

    funeralHomeOptions: List<FuneralHomeOption>,
    eventTypeOptions: List<String>,
    religionTypeOptions: List<String>,

    funeralHome: String,
    eventType: String,
    patientName: String,
    age: String,
    religion: String,
    onFuneralHomeChange: (FuneralHomeOption) -> Unit,
    onEventTypeChange: (String) -> Unit,
    onPatientNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onReligionChange: (String) -> Unit
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
                text = "행사 상세",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ),
            )
            Spacer(modifier = Modifier.width(58.dp))
            Column (){

                Row (modifier = Modifier.fillMaxWidth()){
                    Box (modifier = Modifier.weight(1f)){
                        // 1. 장례식장 (중요도가 높고 이름이 길 수 있으므로 1단 배치)
                        CustomDropdownField(
                            label = "장례식장",
                            options = funeralHomeOptions.map { it.name },
                            selectedOption = funeralHome,
                            onOptionSelected = { name ->
                                funeralHomeOptions.find { it.name == name }
                                    ?.let { onFuneralHomeChange(it) }
                            },
                            placeholder = "선택",
                            height = 48.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CustomDropdownField(
                            label = "행사형태",
                            options = eventTypeOptions,
                            selectedOption = eventType,
                            onOptionSelected = onEventTypeChange,
                            placeholder = "선택",
                            height = 48.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CustomDropdownField(
                            label = "종교",
                            options = religionTypeOptions,
                            selectedOption = religion,
                            onOptionSelected = onReligionChange,
                            placeholder = "선택",
                            height = 48.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. 환자명 & 연령 (고인 정보 묶음 - 2단 배치)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            label = "환자명",
                            value = patientName,
                            onValueChange = onPatientNameChange,
                            placeholder = "이름 입력",
                            height = 48.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            label = "연령",
                            value = age,
                            onValueChange = onAgeChange,
                            placeholder = "세",
                            height = 48.dp
                        )
                    }
                }
            }
        }
    }
}