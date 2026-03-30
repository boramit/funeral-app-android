package com.example.boram_funeral.ui.screens.contract.model

/**
 * 유족 인적사항 1행의 펜 입력 tick 모음
 * tick 값이 바뀔 때마다 해당 HandwrittenCell이 재구성됩니다.
 */
data class SurvivorRowTick(
    val relationTick: Int = 0,      // 사망자와의 관계
    val nameTick: Int = 0,          // 동의자 성명
    val birthDateTick: Int = 0,     // 생년월일
    val addressTick: Int = 0,       // 주소
    val signatureTick: Int = 0,     // 정보제공 동의 서명
)

/**
 * 서면 유가족 정보 각 칸의 펜 입력 tick 모음
 * 각 구분별 6칸 (행) × 2열 (좌/우)
 */
data class FamilyTicks(
    val chiefMournerTicks:   List<Int> = List(6) { 0 },  // 상주
    val daughterInLawTicks:  List<Int> = List(6) { 0 },  // 자부
    val etcTicks:            List<Int> = List(6) { 0 },  // 기타
    val daughterTicks:       List<Int> = List(6) { 0 },  // 여식
    val sonInLawTicks:       List<Int> = List(6) { 0 },  // 사위
    val extra1Ticks:         List<Int> = List(6) { 0 },  // 빈 라벨
    val extra2Ticks:         List<Int> = List(6) { 0 },  // 빈 라벨
    val extra3Ticks:         List<Int> = List(6) { 0 },  // 빈 라벨
    val extra4Ticks:         List<Int> = List(6) { 0 },  // 빈 라벨
)