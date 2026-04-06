package com.example.boram_funeral.ui.screens.counsel.logic

import com.example.boram_funeral.ui.screens.counsel.model.ProductItem
import com.example.boram_funeral.ui.screens.counsel.model.defaultProductMap
import com.example.boram_funeral.ui.screens.counsel.model.productCategories

// ── CounselViewModel 상태 ─────────────────────────────────────────────────────

data class CounselUiState(
    // 이동/일정 정보
    val deceasedName: String = "",
    val burialDate: String = "",
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val departureDate: String = "",
    val selectedTransport: String = "",
    val selectedPriority: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val selectedFuneral: String = "",

    // 상단 배너 / 행사 요약 표시용
    val affiliate: String = "",         // 계열사
    val mortuary: String = "",          // 빈소
    val eventType: String = "",         // 행사 형태
    val chiefMourner: String = "",      // 상주
    val productProgress: String = "",   // 상품진행
    val specialNote: String = "",       // 특이사항

    // 정산 내역
    val cardPayment: String = "",       // 카드
    val cashPayment: String = "",       // 현금
    val totalAmount: String = "",       // 총 금액
)

// ── CounselingViewModel 상태 ──────────────────────────────────────────────────

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
    val agreementDetail: String = "",
)

// ── ConsultationViewModel 상태 ────────────────────────────────────────────────

data class ConsultationUiState(
    val selectedTabIndex: Int = 0,
    val products: Map<String, List<ProductItem>> = defaultProductMap(),
    val selectedProduct: ProductItem? = null,       // null이면 모달 닫힘
    val selectedProductIds: Set<Int> = emptySet(),  // 확인된 상품 ID 목록
) {
    // 현재 탭의 카테고리명 (index 0 = 이용계약서, 1+ = 상품 카테고리)
    val currentCategory: String
        get() = productCategories.getOrElse(selectedTabIndex - 1) { "" }

    val currentProducts: List<ProductItem>
        get() = products[currentCategory] ?: emptyList()

    // 확인된 상품 전체 목록 (저장 시 사용)
    val confirmedProducts: List<ProductItem>
        get() = products.values.flatten().filter { it.id in selectedProductIds }
}