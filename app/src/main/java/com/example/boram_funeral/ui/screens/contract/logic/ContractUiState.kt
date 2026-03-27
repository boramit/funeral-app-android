package com.example.boram_funeral.ui.screens.contract.logic

import com.example.boram_funeral.ui.screens.contract.model.ContractData
import com.example.boram_funeral.ui.screens.contract.model.FuneralItem
import com.example.boram_funeral.ui.screens.contract.model.FuneralServiceItem
import com.example.boram_funeral.ui.screens.contract.model.RoomPriceItem
import com.example.boram_funeral.ui.screens.contract.model.ServiceItem
import com.example.boram_funeral.ui.screens.contract.model.defaultFuneralItems
import com.example.boram_funeral.ui.screens.contract.model.defaultLeftItems
import com.example.boram_funeral.ui.screens.contract.model.defaultRightItems
import com.example.boram_funeral.ui.screens.contract.model.defaultRoomPriceItems
import com.example.boram_funeral.ui.screens.contract.model.defaultServiceItems

data class ContractUiState(

    // =========================================================================
    // Step 0 — ReceptionBasicStep (접수 기본 정보)
    // =========================================================================

    /** 부고사유 — SelectCell 선택값 (병사/외인사/자연사/미상/기타/코로나) */
    val deathReason: String = "",
    /** 행사형태 — SelectCell 선택값 (자체 행사/타상조 행사/보람그룹 행사/무빈소 행사/대관 행사) */
    val eventType: String = "",
    /** 유입경로 — 직접 입력 텍스트 */
    val inflowPath: String = "",
    /** 선택된 장례식장 — 기본값 의정부 */
    val selectedFuneralHome: ContractData = ContractData.UIJEONGBU,
    /** 빈소 선택 옵션 — 장례식장 선택 시 자동 교체 */
    val roomOptions: List<String> = ContractData.UIJEONGBU.roomOptions,
    val roomName: String = "",
    /** 상주명 */
    val chiefMourner: String = "",
    /** 고인명 — Step 0 접수 시 입력, Step 1/Step 4와 공유 */
    val deceasedName: String = "",
    /** 장례지도사명 */
    val directorName: String = "",


    // =========================================================================
    // Step 1 — DeceasedDetailStep (장례식장 이용 계약서)
    // =========================================================================

    /** 상주 성함 — 계약서 상단 "상주 OOO님을(를) 이용자로 하여" 부분 */
    val chiefMournerName: String = "",
    /** 본관 */
    val bongwan: String = "",
    /** 연령 */
    val age: String = "",
    /** 성별 */
    val gender: String = "",
    /** 종교 */
    val religion: String = "",
    /** 직분/세례명 */
    val baptismalName: String = "",
    /** 주민번호 — 고인 */
    val jumin: String = "",
    /** 주소 — 고인 */
    val address: String = "",
    /** 사망장소 */
    val deathPlace: String = "",
    /** 사망일시 */
    val deathDateTime: String = "",

    /** 임차인(계약자)명 */
    val contractorName: String = "",
    /** 임차인 주민등록번호 */
    val contractorJumin: String = "",
    /** 임차인 관계 */
    val contractorRelation: String = "",
    /** 임차인 집전화 */
    val contractorHomeTel: String = "",
    /** 임차인 휴대폰 */
    val contractorMobile: String = "",
    /** 임차인 주소 */
    val contractorAddress: String = "",

    /** 계약기간 시작 — 월/일 */
    val contractStartMonth: String = "",
    val contractStartDay: String = "",
    /** 계약기간 종료 — 월/일 */
    val contractEndMonth: String = "",
    val contractEndDay: String = "",
    /** 계약기간 년도 — 오늘 날짜로 초기값 세팅 */
    val year: String = "",

    /** 안치실 */
    val mortuary: String = "",
    /** 안치일시 — 월/일/시/분 */
    val mortuaryMonth: String = "",
    val mortuaryDay: String = "",
    val mortuaryHour: String = "",
    val mortuaryMinute: String = "",

    /** 빈소 (제3조) */
    val funeralRoom: String = "",
    /** 입관일시 — 월/일/시/분 */
    val coffinMonth: String = "",
    val coffinDay: String = "",
    val coffinHour: String = "",
    val coffinMinute: String = "",

    /** 장지 */
    val burialPlace: String = "",
    /** 발인일시 — 월/일/시/분 */
    val departureMonth: String = "",
    val departureDay: String = "",
    val departureHour: String = "",
    val departureMinute: String = "",

    /** 빈소입실 일시 — 월/일/시/분 */
    val checkInMonth: String = "",
    val checkInDay: String = "",
    val checkInHour: String = "",
    val checkInMinute: String = "",

    /** 빈소임대료 호실 리스트 */
    val roomPriceItems: List<RoomPriceItem> = defaultRoomPriceItems(),
    /** 부대시설 이용료 리스트 */
    val serviceItems: List<ServiceItem> = defaultServiceItems(),

    /** 서명 다이얼로그 표시 여부 */
    val isSignatureDialogVisible: Boolean = false,
    /** 서명 리컴포지션 트리거 (Path는 remember로 관리) */
    val signatureUpdateTick: Int = 0,


    // =========================================================================
    // Step 2 — CasketShroudStep (관/수의)
    // =========================================================================

    /** 좌측 품목 리스트 */
    val leftItems: List<FuneralServiceItem> = defaultLeftItems(),
    /** 우측 품목 리스트 */
    val rightItems: List<FuneralServiceItem> = defaultRightItems(),


    // =========================================================================
    // Step 3 — FoodCateringStep (음식/케이터링)
    // =========================================================================
    // TODO: FoodCateringStep 코드 확인 후 필드 추가 예정
    // val cateringMemo: String = "",


    // =========================================================================
    // Step 4 — FuneralContractStep (매점용품 리스트)
    // =========================================================================

    /** 빈소 호실 */
    val roomNumber: String = "",
    /** 매점용품 리스트 (셋팅수량 고정, 반품수량 입력) */
    val items: List<FuneralItem> = defaultFuneralItems(),
) {
    // 매점용품 금액 자동 계산
    val settingAmount: Long get() = items.sumOf { it.unitPrice * it.settingQuantity }
    val returnAmount: Long  get() = items.sumOf { it.unitPrice * it.returnQuantity }
    val totalAmount: Long   get() = settingAmount - returnAmount
}

