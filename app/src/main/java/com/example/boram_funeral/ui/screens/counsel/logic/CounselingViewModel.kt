// CounselingViewModel.kt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



class CounselingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CounselingUiState())
    val uiState = _uiState.asStateFlow()
    fun updateField(transform: (CounselingUiState) -> CounselingUiState) {
        _uiState.value = transform(_uiState.value)
    }
    fun clearAllInputs() {
        _uiState.value = CounselingUiState() // 새 객체로 초기화
    }

    val counselingTypeOptions = listOf("현장상담", "유선상담", "기타")
    val relationshipOptions = listOf("부부", "자식", "형제", "자매", "어머니")

    val funeralHomeOptions = listOf("보람의정부장례식장", "보람세민에스장례식장", "보람인천장례식장")
    val eventTypeOptions = listOf("자체 행사", "외부 행사", "기타")

    val religionTypeOptions = listOf("기독교","불교","천주교","무교","개신교","원불교","기타")
    

    data class CounselingUiState(
        // ① 기본 정보
        val counselingNo: String = "20240304-001",
        val counselingType: String = "",
        val customerName: String = "",
        val phoneNumber: String = "",
        val relationship: String = "",

        // ② 행사 상세
        val funeralHome: String = "",
        val eventType: String = "",
        val patientName: String = "",
        val age: String = "",
        val religion: String = "",

        // ③ 장소 및 기관
        val locationAdmission: String = "",
        val locationCare: String = "",
        val funeralCompany: String = "",

        // ④ 할인 및 협약
        val saleStatus: String = "미적용",
        val saleCategory: String = "",
        val companyName: String = "",
        val agreementDetail: String = ""
    )


    // 저장 로직 예시
    fun saveCounselingInfo() {
        val currentState = _uiState.value

        // 1. 간단하게 전체 출력
        println("Counseling Data: $currentState")

        // 2. 안드로이드 로그캣(Logcat)으로 가독성 있게 출력
        android.util.Log.d("CounselingSave", """
        ================================================
        [상담 정보 저장 요청]
        ------------------------------------------------
        1. 기본 정보
           - 번호: ${currentState.counselingNo}
           - 유형: ${currentState.counselingType}
           - 고객: ${currentState.customerName} (${currentState.relationship})
           - 연락처: ${currentState.phoneNumber}
        
        2. 행사 상세
           - 장례식장: ${currentState.funeralHome}
           - 형태: ${currentState.eventType}
           - 대상자: ${currentState.patientName} (${currentState.age}세)
           - 종교: ${currentState.religion}
        
        3. 장소 및 기관
           - 유치장소: ${currentState.locationAdmission}
           - 요양장소: ${currentState.locationCare}
           - 상조회사: ${currentState.funeralCompany}
        
        4. 할인 및 협약
           - 상태: ${currentState.saleStatus}
           - 구분: ${currentState.saleCategory}
           - 단체명: ${currentState.companyName}
           - 상세내용: ${currentState.agreementDetail}
        ================================================
    """.trimIndent())

        clearAllInputs()
        // 여기에 나중에 실제 repository.save(data)를 넣으시면 됩니다.
    }
}