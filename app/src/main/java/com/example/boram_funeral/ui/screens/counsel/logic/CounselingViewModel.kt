package com.example.boram_funeral.ui.screens.counsel.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boram_funeral.ui.screens.counsel.model.FuneralHomeOption
import com.example.boram_funeral.ui.screens.counsel.model.funeralHomeList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounselingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CounselingUiState())
    val uiState = _uiState.asStateFlow()

    val counselingTypeOptions = listOf("현장상담", "유선상담", "기타")
    val relationshipOptions   = listOf("부부", "자식", "형제", "자매", "어머니")
    val funeralHomeOptions: List<FuneralHomeOption> = funeralHomeList
    val eventTypeOptions      = listOf("자체 행사", "외부 행사", "기타")
    val religionTypeOptions   = listOf("기독교", "불교", "천주교", "무교", "개신교", "원불교", "기타")

    fun updateField(transform: (CounselingUiState) -> CounselingUiState) {
        _uiState.update(transform)
    }

    fun clearAllInputs() {
        _uiState.value = CounselingUiState()
    }

    fun saveCounselingInfo() {
        val s = _uiState.value
        Log.d("CounselingSave", """
            ================================================
            [상담 정보 저장 요청]
            1. 기본 정보
               번호: ${s.counselingNo} | 유형: ${s.counselingType}
               고객: ${s.customerName} (${s.relationship}) | 연락처: ${s.phoneNumber}
            2. 행사 상세
               장례식장: ${s.funeralHome} | 형태: ${s.eventType}
               대상자: ${s.patientName} (${s.age}세) | 종교: ${s.religion}
            3. 장소 및 기관
               유치: ${s.locationAdmission} | 요양: ${s.locationCare} | 상조: ${s.funeralCompany}
            4. 할인 및 협약
               상태: ${s.saleStatus} | 구분: ${s.saleCategory}
               단체: ${s.companyName} | 상세: ${s.agreementDetail}
            ================================================
        """.trimIndent())
        clearAllInputs()
    }
}