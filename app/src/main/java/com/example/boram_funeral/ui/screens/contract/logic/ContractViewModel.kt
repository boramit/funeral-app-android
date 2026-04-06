package com.example.boram_funeral.ui.screens.contract.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boram_funeral.ui.screens.contract.model.ContractData
import com.example.boram_funeral.ui.screens.contract.model.FamilyTicks
import com.example.boram_funeral.ui.screens.contract.model.RoomType
import com.example.boram_funeral.ui.screens.contract.model.SurvivorRowTick
import com.example.boram_funeral.ui.screens.contract.model.defaultRoomPriceItems
import com.example.boram_funeral.ui.screens.contract.model.defaultServiceItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class ContractViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ContractUiState(
            // 오늘 날짜로 초기값 세팅
            year  = Calendar.getInstance().get(Calendar.YEAR).toString(),
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _isContractOpen = MutableStateFlow(false)
    val isContractOpen = _isContractOpen.asStateFlow()

    fun openContract() { _isContractOpen.value = true }
    fun closeContract() { _isContractOpen.value = false }

    private val _isPdfCapturing = MutableStateFlow(false)
    val isPdfCapturing = _isPdfCapturing.asStateFlow()

    fun startPdfCapture() { _isPdfCapturing.value = true }
    fun endPdfCapture()   { _isPdfCapturing.value = false }

    // 공통 상태 업데이트 함수 — 모든 Step에서 이 함수를 통해 상태 변경
    fun updateField(transform: (ContractUiState) -> ContractUiState) {
        _uiState.value = transform(_uiState.value)
    }

    // =========================================================================
    // Step 0 — ReceptionBasicStep (접수 기본 정보)
    // =========================================================================

    /** 부고사유 선택 — SelectCell에서 호출 */
    fun updateDeathReason(value: String) =
        updateField { it.copy(deathReason = value) }

    /** 행사형태 선택 — SelectCell에서 호출 */
    fun updateEventType(value: String) =
        updateField { it.copy(eventType = value) }

    /** 유입경로 입력 — InputCell에서 호출 */
    fun updateInflowPath(value: String) =
        updateField { it.copy(inflowPath = value) }

    /** 빈소 선택 — SelectCell에서 호출 */
    fun updateRoomName(value: String) =
        updateField { state ->
            val roomType = if (value.contains("VIP") || value.contains("VVIP"))
                RoomType.VIPVVIP else RoomType.NORMAL
            state.copy(
                roomName     = value,
                serviceItems = defaultServiceItems(roomType)
            )
        }

    /** 장례식장 선택 — 빈소 옵션 / 호실 데이터 / 부대시설 단가 자동 교체 */
    fun updateFuneralHome(funeralHome: ContractData) =
        updateField { state ->
            state.copy(
                selectedFuneralHome = funeralHome,
                roomOptions         = funeralHome.roomOptions,
                roomPriceItems      = defaultRoomPriceItems(funeralHome),
                serviceItems        = defaultServiceItems(),
            )
        }

    /** 외부 ViewModel에서 장례식장명(String)으로 전달받을 때 사용 */
    fun updateFuneralHomeByName(name: String) =
        updateFuneralHome(ContractData.fromDisplayName(name))

    /** 상주명 입력 */
    fun updateChiefMourner(value: String) =
        updateField { it.copy(chiefMourner = value) }

    /** 고인명 입력 — Step 0 접수 시 입력, Step 1/Step 4 헤더에서도 동일하게 표시됨 */
    fun updateDeceasedName(value: String) =
        updateField { it.copy(deceasedName = value) }

    /** 장례지도사명 입력 */
    fun updateDirectorName(value: String) =
        updateField { it.copy(directorName = value) }



    // =========================================================================
    // Step 1 — UseContractTableInputTest (장례식장 이용 계약서)
    // =========================================================================

    /** 상주 성함 — 계약서 상단 문구용 */
    fun updateChiefMournerName(value: String) = updateField { it.copy(chiefMourner = value) }

    /** 고인 인적사항 */
    fun updateBongwan(value: String) = updateField { it.copy(bongwan = value) }
    fun updateAge(value: String) = updateField { it.copy(age = value) }
    fun updateGender(value: String) = updateField { it.copy(gender = value) }
    fun updateReligion(value: String) = updateField { it.copy(religion = value) }
    fun updateBaptismalName(value: String) = updateField { it.copy(baptismalName = value) }
    fun updateJumin(value: String) = updateField { it.copy(jumin = value) }
    fun updateAddress(value: String) = updateField { it.copy(address = value) }
    fun updateDeathPlace(value: String) = updateField { it.copy(deathPlace = value) }
    fun updateDeathDateTime(value: String) = updateField { it.copy(deathDateTime = value) }

    /** 임차인(계약자) 정보 */
    fun updateContractorName(value: String) = updateField { it.copy(contractorName = value) }
    fun updateContractorJumin(value: String) = updateField { it.copy(contractorJumin = value) }
    fun updateContractorRelation(value: String) = updateField { it.copy(contractorRelation = value) }
    fun updateContractorHomeTel(value: String) = updateField { it.copy(contractorHomeTel = value) }
    fun updateContractorMobile(value: String) = updateField { it.copy(contractorMobile = value) }
    fun updateContractorAddress(value: String) = updateField { it.copy(contractorAddress = value) }

    /** 계약기간 — 년도는 공통, 시작/종료 월일 분리 */
    fun updateYear(value: String) = updateField { it.copy(year = value) }
    fun updateContractStartMonth(value: String) = updateField { it.copy(contractStartMonth = value) }
    fun updateContractStartDay(value: String) = updateField { it.copy(contractStartDay = value) }
    fun updateContractEndMonth(value: String) = updateField { it.copy(contractEndMonth = value) }
    fun updateContractEndDay(value: String) = updateField { it.copy(contractEndDay = value) }

    /** 안치일시 */
    fun updateMortuary(value: String) = updateField { it.copy(mortuary = value) }
    fun updateMortuaryMonth(value: String) = updateField { it.copy(mortuaryMonth = value) }
    fun updateMortuaryDay(value: String) = updateField { it.copy(mortuaryDay = value) }
    fun updateMortuaryHour(value: String) = updateField { it.copy(mortuaryHour = value) }
    fun updateMortuaryMinute(value: String) = updateField { it.copy(mortuaryMinute = value) }

    /** 빈소/입관일시 */
    fun updateFuneralRoom(value: String) = updateField { it.copy(funeralRoom = value) }
    fun updateCoffinMonth(value: String) = updateField { it.copy(coffinMonth = value) }
    fun updateCoffinDay(value: String) = updateField { it.copy(coffinDay = value) }
    fun updateCoffinHour(value: String) = updateField { it.copy(coffinHour = value) }
    fun updateCoffinMinute(value: String) = updateField { it.copy(coffinMinute = value) }

    /** 장지/발인일시 */
    fun updateBurialPlace(value: String) = updateField { it.copy(burialPlace = value) }
    fun updateDepartureMonth(value: String) = updateField { it.copy(departureMonth = value) }
    fun updateDepartureDay(value: String) = updateField { it.copy(departureDay = value) }
    fun updateDepartureHour(value: String) = updateField { it.copy(departureHour = value) }
    fun updateDepartureMinute(value: String) = updateField { it.copy(departureMinute = value) }

    /** 빈소입실 일시 */
    fun updateCheckInMonth(value: String) = updateField { it.copy(checkInMonth = value) }
    fun updateCheckInDay(value: String) = updateField { it.copy(checkInDay = value) }
    fun updateCheckInHour(value: String) = updateField { it.copy(checkInHour = value) }
    fun updateCheckInMinute(value: String) = updateField { it.copy(checkInMinute = value) }

    /** 특정 호실의 일수 업데이트 */
    fun updateRoomDayCount(roomName: String, dayCount: String) =
        updateField { state ->
            state.copy(
                roomPriceItems = state.roomPriceItems.map { item ->
                    if (item.roomName == roomName) {
                        // "624,000원" → 624000 으로 변환 후 계산
                        val cleaned = item.pricePerDay
                            .replace(",", "")
                            .replace("원", "")
                            .trim()
                        val priceNum = cleaned.toLongOrNull() ?: 0L
                        val count = dayCount.trim().toLongOrNull() ?: 0L
                        val calculated = if (count > 0 && priceNum > 0) {
                            val total = priceNum * count
                            java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA).format(total) + "원"
                        } else ""
                        item.copy(dayCount = dayCount, totalAmount = calculated)
                    } else item
                }
            )
        }
    /** 부대시설 이용료 — 수량 업데이트 */
    fun updateServiceCount(title: String, count: String) =
        updateField { state ->
            state.copy(
                serviceItems = state.serviceItems.map { item ->
                    if (item.title == title) {
                        val cnt = count.toLongOrNull() ?: 0L
                        val calculated = if (cnt > 0 && item.unitPrice > 0) {
                            java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA)
                                .format(item.unitPrice * cnt) + "원"
                        } else ""
                        item.copy(count = count, totalAmount = calculated)
                    } else item
                }
            )
        }

    /** 부대시설 이용료 — 금액 업데이트 */
    fun updateServiceAmount(title: String, amount: String) =
        updateField { state ->
            state.copy(
                serviceItems = state.serviceItems.map { item ->
                    if (item.title == title) item.copy(totalAmount = amount)
                    else item
                }
            )
        }


    /** 서명 다이얼로그 열기 */
    fun showSignatureDialog() =
        updateField { it.copy(isSignatureDialogVisible = true) }

    /** 서명 다이얼로그 닫기 */
    fun dismissSignatureDialog() =
        updateField { it.copy(isSignatureDialogVisible = false) }

    /** 서명 완료 — 다이얼로그 닫고 tick 증가로 리컴포지션 유도
     *  Path 객체는 View 레이어에서 remember로 관리하고
     *  tick 값 변화로 ContractFooter가 다시 그려지도록 트리거만 함 */
    fun confirmSignature() =
        updateField {
            it.copy(
                isSignatureDialogVisible = false,
                signatureUpdateTick = it.signatureUpdateTick + 1
            )
        }


    // =========================================================================
    // Step 2 — CasketShroudStep (관/수의)
    // =========================================================================

    /** 좌측 품목 수량 업데이트 */
    fun updateLeftItemQuantity(name: String, quantity: String) =
        updateField { state ->
            state.copy(
                leftItems = state.leftItems.map { item ->
                    if (item.name == name) item.copy(quantity = quantity) else item
                }
            )
        }

    /** 우측 품목 수량 업데이트 */
    fun updateRightItemQuantity(name: String, quantity: String) =
        updateField { state ->
            state.copy(
                rightItems = state.rightItems.map { item ->
                    if (item.name == name) item.copy(quantity = quantity) else item
                }
            )
        }

    /** 우측 품목 비고 업데이트 */
    fun updateRightItemRemarks(name: String, remarks: String) =
        updateField { state ->
            state.copy(
                rightItems = state.rightItems.map { item ->
                    if (item.name == name) item.copy(remarks = remarks) else item
                }
            )
        }


    // =========================================================================
    // Step 3 — FoodCateringStep (음식/케이터링)
    // =========================================================================

    // TODO: FoodCateringStep 코드 확인 후 상태 필드 추가 예정
    // 예시:
    // fun updateCateringMemo(value: String) =
    //     updateField { it.copy(cateringMemo = value) }


    // =========================================================================
    // Step 4 — FuneralContractStep (매점용품 리스트)
    // =========================================================================

    /** 빈소 호실 */
    fun updateRoomNumber(value: String) =
        updateField { it.copy(roomNumber = value) }

    /** 반품수량 업데이트 — 0 미만 또는 셋팅수량 초과 방지 */
    fun updateReturnQuantity(itemNumber: Int, qty: Int) =
        updateField { state ->
            state.copy(
                items = state.items.map { item ->
                    if (item.number == itemNumber)
                        item.copy(returnQuantity = qty.coerceIn(0, item.settingQuantity))
                    else item
                }
            )
        }

    /** 반품수량 전체 초기화 */
    fun resetReturnQuantities() =
        updateField { state ->
            state.copy(items = state.items.map { it.copy(returnQuantity = 0) })
        }


    // =========================================================================
    // Step 6 — FuneraltermsStep (이용약관)
    // =========================================================================

    /** 서명 다이얼로그 열기 */
    fun showTermsSignatureDialog() =
        updateField { it.copy(termsIsSignatureDialogVisible = true) }

    /** 서명 다이얼로그 닫기 */
    fun dismissTermsSignatureDialog() =
        updateField { it.copy(termsIsSignatureDialogVisible = false) }

    /** 서명 완료 — 다이얼로그 닫고 tick 증가로 리컴포지션 유도
     *  Path 객체는 View 레이어에서 remember로 관리하고
     *  tick 값 변화로 TermsFooter가 다시 그려지도록 트리거만 함 */
    fun confirmTermsSignature() =
        updateField {
            it.copy(
                termsIsSignatureDialogVisible = false,
                termsSignatureUpdateTick = it.termsSignatureUpdateTick + 1
            )
        }

    // =========================================================================
    // Step 7 — PrivacyConsentStep (개인정보동의) ViewModel 함수
    // =========================================================================

    // ── 1. 개인정보 수집·이용 동의 (필수) ────────────────────────────────────
    fun updatePrivacyCollectionAgree(agreed: Boolean) =
        _uiState.update { it.copy(privacyCollectionAgree = agreed) }

    // ── 2. 상품 홍보 동의 (선택) ──────────────────────────────────────────────
    fun updatePrivacyMarketingAgree(agreed: Boolean) =
        _uiState.update { it.copy(privacyMarketingAgree = agreed) }

    // ── 3. 고유식별번호 수집·이용 동의 ───────────────────────────────────────
    fun updatePrivacyIdNumberAgree(agreed: Boolean) =
        _uiState.update { it.copy(privacyIdNumberAgree = agreed) }

    // ── 4. 제3자 제공 동의 — 전체 일괄 ──────────────────────────────────────
    fun updatePrivacyThirdPartyAgree(agreed: Boolean) =
        _uiState.update { it.copy(privacyThirdPartyAgree = agreed) }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    fun showPrivacySignatureDialog() =
        _uiState.update { it.copy(isPrivacySignatureDialogVisible = true) }

    fun dismissPrivacySignatureDialog() =
        _uiState.update { it.copy(isPrivacySignatureDialogVisible = false) }

    fun confirmPrivacySignature() =
        _uiState.update {
            it.copy(
                privacySignatureUpdateTick = it.privacySignatureUpdateTick + 1,
                isPrivacySignatureDialogVisible = false
            )
        }

    // =========================================================================
    // Step 8 — DeceasedInfoConsentStep (사망자 정보 제공동의서) ViewModel 함수
    // =========================================================================

    // ── 4. 사망자 인적사항 펜 입력 tick 갱신 ─────────────────────────────────
    fun tickDeceasedName()         = _uiState.update { it.copy(deceasedNameTick = it.deceasedNameTick + 1) }
    fun tickDeceasedGender()       = _uiState.update { it.copy(deceasedGenderTick = it.deceasedGenderTick + 1) }
    fun tickDeceasedIdNumber()     = _uiState.update { it.copy(deceasedIdNumberTick = it.deceasedIdNumberTick + 1) }
    fun tickDeceasedAddress()      = _uiState.update { it.copy(deceasedAddressTick = it.deceasedAddressTick + 1) }
    fun tickDeceasedDeathDate()    = _uiState.update { it.copy(deceasedDeathDateTick = it.deceasedDeathDateTick + 1) }
    fun tickFacilityFromYear()  = _uiState.update { it.copy(facilityFromYearTick  = it.facilityFromYearTick  + 1) }
    fun tickFacilityFromMonth() = _uiState.update { it.copy(facilityFromMonthTick = it.facilityFromMonthTick + 1) }
    fun tickFacilityFromDay()   = _uiState.update { it.copy(facilityFromDayTick   = it.facilityFromDayTick   + 1) }
    fun tickFacilityToYear()    = _uiState.update { it.copy(facilityToYearTick    = it.facilityToYearTick    + 1) }
    fun tickFacilityToMonth()   = _uiState.update { it.copy(facilityToMonthTick   = it.facilityToMonthTick   + 1) }
    fun tickFacilityToDay()     = _uiState.update { it.copy(facilityToDayTick     = it.facilityToDayTick     + 1) }

    // ── 5. 유족 행 펜 입력 tick 갱신 ─────────────────────────────────────────
    private fun updateSurvivorRow(index: Int, update: SurvivorRowTick.() -> SurvivorRowTick) {
        _uiState.update { state ->
            val updated = state.survivorRows.toMutableList()
            updated[index] = updated[index].update()
            state.copy(survivorRows = updated)
        }
    }

    fun tickSurvivorRelation(index: Int)  = updateSurvivorRow(index) { copy(relationTick = relationTick + 1) }
    fun tickSurvivorName(index: Int)      = updateSurvivorRow(index) { copy(nameTick = nameTick + 1) }
    fun tickSurvivorBirthDate(index: Int) = updateSurvivorRow(index) { copy(birthDateTick = birthDateTick + 1) }
    fun tickSurvivorAddress(index: Int)   = updateSurvivorRow(index) { copy(addressTick = addressTick + 1) }
    fun tickSurvivorSignature(index: Int) = updateSurvivorRow(index) { copy(signatureTick = signatureTick + 1) }

    // ── 하단 서명 다이얼로그 ──────────────────────────────────────────────────
    fun showDeceasedSignatureDialog() =
        _uiState.update { it.copy(isDeceasedSignatureDialogVisible = true) }

    fun dismissDeceasedSignatureDialog() =
        _uiState.update { it.copy(isDeceasedSignatureDialogVisible = false) }

    fun confirmDeceasedSignature() =
        _uiState.update {
            it.copy(
                deceasedSignatureUpdateTick = it.deceasedSignatureUpdateTick + 1,
                isDeceasedSignatureDialogVisible = false
            )
        }

    // =========================================================================
    // Step 9 — CustomerConfirmStep (고객확인서) ViewModel 함수
    // =========================================================================

    /** 설명 여부 칸 펜 입력 tick 갱신 */
    fun tickConfirmExplanation(index: Int) {
        _uiState.update { state ->
            val updated = state.confirmExplanationTicks.toMutableList()
            updated[index] = updated[index] + 1
            state.copy(confirmExplanationTicks = updated)
        }
    }

    // ── 서명 다이얼로그 ───────────────────────────────────────────────────────
    fun showConfirmSignatureDialog() =
        _uiState.update { it.copy(isConfirmSignatureDialogVisible = true) }

    fun dismissConfirmSignatureDialog() =
        _uiState.update { it.copy(isConfirmSignatureDialogVisible = false) }

    fun confirmConfirmSignature() =
        _uiState.update {
            it.copy(
                confirmSignatureUpdateTick = it.confirmSignatureUpdateTick + 1,
                isConfirmSignatureDialogVisible = false
            )
        }

    // =========================================================================
    // Step 10 — FamilyInfoStep (서면 유가족 정보) ViewModel 함수
    // =========================================================================

    private fun updateFamilyTicks(update: FamilyTicks.() -> FamilyTicks) {
        _uiState.update { it.copy(familyTicks = it.familyTicks.update()) }
    }

    private fun tickListItem(list: List<Int>, index: Int): List<Int> =
        list.toMutableList().also { it[index] = it[index] + 1 }

    fun tickChiefMourner(index: Int)   = updateFamilyTicks { copy(chiefMournerTicks  = tickListItem(chiefMournerTicks,  index)) }
    fun tickDaughterInLaw(index: Int)  = updateFamilyTicks { copy(daughterInLawTicks = tickListItem(daughterInLawTicks, index)) }
    fun tickEtc(index: Int)            = updateFamilyTicks { copy(etcTicks           = tickListItem(etcTicks,           index)) }
    fun tickDaughter(index: Int)       = updateFamilyTicks { copy(daughterTicks       = tickListItem(daughterTicks,      index)) }
    fun tickSonInLaw(index: Int)       = updateFamilyTicks { copy(sonInLawTicks       = tickListItem(sonInLawTicks,      index)) }
    fun tickExtra1(index: Int)         = updateFamilyTicks { copy(extra1Ticks         = tickListItem(extra1Ticks,        index)) }
    fun tickExtra2(index: Int)         = updateFamilyTicks { copy(extra2Ticks         = tickListItem(extra2Ticks,        index)) }
    fun tickExtra3(index: Int)         = updateFamilyTicks { copy(extra3Ticks         = tickListItem(extra3Ticks,        index)) }
    fun tickExtra4(index: Int)         = updateFamilyTicks { copy(extra4Ticks         = tickListItem(extra4Ticks,        index)) }


    // =========================================================================
    // 공통 — 최종 저장
    // =========================================================================

    /** 저장하기 버튼 클릭 시 호출 — 모든 Step 데이터를 콘솔에 출력 */
    fun saveData(onSuccess: () -> Unit) {
        val s = _uiState.value

        Log.d("ContractSave", "====== 최종 계약서 데이터 ======")

        // Step 0 — 접수 기본 정보
        Log.d("ContractSave", "[Step 0] 부고사유: ${s.deathReason}")
        Log.d("ContractSave", "[Step 0] 행사형태: ${s.eventType}")
        Log.d("ContractSave", "[Step 0] 유입경로: ${s.inflowPath}")
        Log.d("ContractSave", "[Step 0] 빈소: ${s.selectedFuneralHome.displayName}")
        Log.d("ContractSave", "[Step 0] 상주명: ${s.chiefMourner}")
        Log.d("ContractSave", "[Step 0] 고인명: ${s.deceasedName}")
        Log.d("ContractSave", "[Step 0] 지도사명: ${s.directorName}")

        // Step 1 — 장례식장 이용 계약서
        Log.d("ContractSave", "[Step 1] 상주성함: " + s.chiefMournerName)
        Log.d("ContractSave", "[Step 1] 고인명: " + s.deceasedName)
        Log.d("ContractSave", "[Step 1] 본관: " + s.bongwan)
        Log.d("ContractSave", "[Step 1] 연령: " + s.age)
        Log.d("ContractSave", "[Step 1] 성별: " + s.gender)
        Log.d("ContractSave", "[Step 1] 종교: " + s.religion)
        Log.d("ContractSave", "[Step 1] 직분/세례명: " + s.baptismalName)
        Log.d("ContractSave", "[Step 1] 주민번호: " + s.jumin)
        Log.d("ContractSave", "[Step 1] 주소: " + s.address)
        Log.d("ContractSave", "[Step 1] 사망장소: " + s.deathPlace)
        Log.d("ContractSave", "[Step 1] 사망일시: " + s.deathDateTime)
        Log.d("ContractSave", "[Step 1] 계약기간: " + s.year + "년 " + s.contractStartMonth + "월 " + s.contractStartDay + "일 ~ " + s.year + "년 " + s.contractEndMonth + "월 " + s.contractEndDay + "일")
        Log.d("ContractSave", "[Step 1] 임차인: " + s.contractorName)
        Log.d("ContractSave", "[Step 1] 임차인 주민번호: " + s.contractorJumin)
        Log.d("ContractSave", "[Step 1] 임차인 관계: " + s.contractorRelation)
        Log.d("ContractSave", "[Step 1] 임차인 집전화: " + s.contractorHomeTel)
        Log.d("ContractSave", "[Step 1] 임차인 휴대폰: " + s.contractorMobile)
        Log.d("ContractSave", "[Step 1] 임차인 주소: " + s.contractorAddress)
        Log.d("ContractSave", "[Step 1] 안치실: " + s.mortuary)
        Log.d("ContractSave", "[Step 1] 안치일시: " + s.year + "년 " + s.mortuaryMonth + "월 " + s.mortuaryDay + "일 " + s.mortuaryHour + "시 " + s.mortuaryMinute + "분")
        Log.d("ContractSave", "[Step 1] 빈소: " + s.funeralRoom)
        Log.d("ContractSave", "[Step 1] 입관일시: " + s.year + "년 " + s.coffinMonth + "월 " + s.coffinDay + "일 " + s.coffinHour + "시 " + s.coffinMinute + "분")
        Log.d("ContractSave", "[Step 1] 장지: " + s.burialPlace)
        Log.d("ContractSave", "[Step 1] 발인일시: " + s.year + "년 " + s.departureMonth + "월 " + s.departureDay + "일 " + s.departureHour + "시 " + s.departureMinute + "분")
        Log.d("ContractSave", "[Step 1] 빈소입실: " + s.year + "년 " + s.checkInMonth + "월 " + s.checkInDay + "일 " + s.checkInHour + "시 " + s.checkInMinute + "분")
        s.roomPriceItems.filter { it.totalAmount.isNotEmpty() }.forEach { item ->
            Log.d("ContractSave", "[Step 1] 빈소임대료 — " + item.roomName + " / " + item.totalAmount)
        }
        s.serviceItems.filter { it.totalAmount.isNotEmpty() }.forEach { item ->
            Log.d("ContractSave", "[Step 1] 부대시설 — " + item.title + " / 수량: " + item.count + " / 금액: " + item.totalAmount)
        }

        // Step 2 — 관/수의 품목
        s.leftItems.filter { it.quantity.isNotEmpty() }.forEach { item ->
            Log.d("ContractSave", "[Step 2] 좌측 — " + item.name + " / 수량: " + item.quantity)
        }
        s.rightItems.filter { it.quantity.isNotEmpty() }.forEach { item ->
            Log.d("ContractSave", "[Step 2] 우측 — " + item.name + " / 수량: " + item.quantity + " / 비고: " + item.remarks)
        }

        // Step 4 — 매점용품 리스트
        Log.d("ContractSave", "[Step 4] 빈소호실: ${s.roomNumber}")
        Log.d("ContractSave", "[Step 4] 셋팅합계: ${s.settingAmount}원")
        Log.d("ContractSave", "[Step 4] 반품차감: ${s.returnAmount}원")
        Log.d("ContractSave", "[Step 4] 최종합계: ${s.totalAmount}원")
        s.items.filter { it.returnQuantity > 0 }.forEach { item ->
            Log.d("ContractSave", "[Step 4] 반품항목 — ${item.name} / 셋팅: ${item.settingQuantity} / 반품: ${item.returnQuantity} / 금액: ${item.amount}원")
        }

        Log.d("ContractSave", "================================")

        onSuccess()
    }

    /** PDF 저장 완료 후 호출 — 상태 초기화 */
    fun clearData() {
        _uiState.value = ContractUiState(
            year = Calendar.getInstance().get(Calendar.YEAR).toString()
        )
    }
}