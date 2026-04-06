package com.example.boram_funeral.ui.screens.contract.model

// ─── 장례서비스 품목 데이터 (CasketShroudStep) ───────────────────────────────
data class FuneralServiceItem(
    val name: String,
    val unit: String = "",
    val price: String = "",
    val isHeader: Boolean = false,
    val isYellowHeader: Boolean = false,
    val quantity: String = "",     // MutableState 제거 — ViewModel에서 관리
    val isReadOnly: Boolean = false,
    val remarks: String = "",      // MutableState 제거 — ViewModel에서 관리
    val imageResId: Int? = null,   // 품명 클릭 시 표시할 이미지 리소스 ID (null이면 이미지 없음)
    val noModal: Boolean = false   // true이면 품명 클릭 모달 비활성화
)

fun defaultLeftItems() = listOf(
    FuneralServiceItem("1.7 오동화장관",          "개",        "400,000"),
    FuneralServiceItem("2.5 오동맞춤관",          "개",        "600,000"),
    FuneralServiceItem("4.2 오동매장관",          "개",        "480,000"),
    FuneralServiceItem("4.2 솔송매장관",          "개",        "800,000"),
    FuneralServiceItem("4.2 향매장관",            "개",      "1,800,000"),
    FuneralServiceItem("횡대(오동,솔송,향)",       "조",              ""),
    FuneralServiceItem("수의",                    "벌",              ""),
    FuneralServiceItem("멧베(명품/특명품)",  "기본 2필", "300,000 / 400,000", isReadOnly = true),
    FuneralServiceItem("꼬깔(명품)",         "기본 1필",       "200,000", isReadOnly = true),
    FuneralServiceItem("칠성판",                  "개",         "70,000"),
    FuneralServiceItem("수세포",                  "개",         "30,000"),
    FuneralServiceItem("수세이불",                "개",        "100,000"),
    FuneralServiceItem("시신위생약품",             "SET",       "150,000"),
    FuneralServiceItem("베개",                    "개",         "10,000"),
    FuneralServiceItem("배허리띠",                "SET",        "20,000"),
    FuneralServiceItem("액자리본",                "개",         "10,000"),
    FuneralServiceItem("혼백함",                  "개",         "10,000"),
    FuneralServiceItem("위패/명패",               "개",         "30,000"),
    FuneralServiceItem("무연향",                  "BOX",        "20,000"),
    FuneralServiceItem("고급 효원향",             "BOX",        "25,000"),
    FuneralServiceItem("양초",                    "개",          "5,000"),
    FuneralServiceItem("부의록",                  "개",         "20,000"),
    FuneralServiceItem("고급완장세트(두줄)",       "개",              ""),
    FuneralServiceItem("고급완장세트(한줄)",       "개",              ""),
    FuneralServiceItem("알코올",                  "개",         "20,000"),
    FuneralServiceItem("탈지면",                  "개",         "40,000"),
    FuneralServiceItem("한지",                    "권",         "40,000"),
    FuneralServiceItem("입관속옷",                "인",         "80,000"),
    FuneralServiceItem("습신",                    "개",         "10,000"),
    FuneralServiceItem("고급자수염보",            "개",        "150,000"),
    FuneralServiceItem("초석(보공)",              "인",         "40,000"),
    FuneralServiceItem("명정",                    "개",        "100,000"),
    FuneralServiceItem("소창(결관포)",            "마",         "40,000"),
    FuneralServiceItem("다라니경(고급/일반)",     "장",   "50,000 / 10,000"),
)

fun defaultRightItems() = listOf(
    FuneralServiceItem("기독경/천주경",           "장",         "10,000"),
    FuneralServiceItem("윤아",                   "SET",          "5,000"),
    FuneralServiceItem("예단",                   "SET",         "10,000"),
    FuneralServiceItem("축문",                    "권",          "5,000"),
    FuneralServiceItem("운구용 위생킷",           "개",              ""),
    FuneralServiceItem("유골함",                  isHeader = true),
    FuneralServiceItem("유골함",                  "개",              "", noModal = true),
    FuneralServiceItem("각인비",                  "회",              "", noModal = true),
    FuneralServiceItem("전사비",                  "회",              "", noModal = true),
    FuneralServiceItem("영정사진",                isHeader = true),
    FuneralServiceItem("인화",                    "회",        "100,000", noModal = true),
    FuneralServiceItem("액자",                    "회",         "40,000", noModal = true),
    FuneralServiceItem("빈소 LED 영정",           "개",         "60,000", noModal = true),
    FuneralServiceItem("장의차량",                isHeader = true),
    FuneralServiceItem("리무진 , 버스",            "",        "1,500,000", noModal = true),
    FuneralServiceItem("이송비",                   "",              "", noModal = true),
    FuneralServiceItem("비아젬",                  isHeader = true),
    FuneralServiceItem("비아젬",                   "",              "", noModal = true),
    FuneralServiceItem("오마주",                   "",              "", noModal = true),
    FuneralServiceItem("헤리티지박스",             "",              "", noModal = true),
)


// ─── 빈소임대료 호실 데이터 ───────────────────────────────────────────────────
data class RoomPriceItem(
    val roomName: String,          // 호실명
    val seatCount: String,         // 좌석수
    val size: String,              // 평형
    val pricePerHour: String,      // 시간당 요금
    val pricePerDay: String,       // 일당 요금
    val dayCount: String = "",     // 일수 (입력값)
    val totalAmount: String = "",  // 금액 (자동 계산)
)

// ─── 부대시설 이용료 데이터 ───────────────────────────────────────────────────
data class ServiceItem(
    val title: String,             // 서비스명
    val price: String,             // 요금 설명 (화면 표시용)
    val unitText: String,          // 단위 (일/회)
    val unitPrice: Long = 0L,      // 실제 계산에 사용할 단가
    val count: String = "",        // 수량 입력값
    val totalAmount: String = "",  // 자동 계산된 금액
)

// ─── 호실 타입 (오물수거료 단가 분기용) ──────────────────────────────────────
enum class RoomType { VIPVVIP, NORMAL }

fun getRoomType(roomName: String): RoomType =
    if (roomName.contains("VIP") || roomName.contains("VVIP")) RoomType.VIPVVIP
    else RoomType.NORMAL

// ─── 장례식장 Enum ────────────────────────────────────────────────────────────
enum class ContractData(
    val displayName: String,
    val roomOptions: List<String>,
) {
    UIJEONGBU(
        displayName = "보람의정부장례식장",
        roomOptions = listOf("특실", "201호실", "202호실", "203호실", "VIP호실", "VVIP호실", "VIP+VVIP호실"),
    ),
    SEMIN(
        displayName = "보람세민에스장례식장",
        roomOptions = listOf("1호실", "2호실", "3호실"),   // TODO: 실제 데이터로 교체
    ),
    INCHEON(
        displayName = "보람인천장례식장",
        roomOptions = listOf("A호실", "B호실", "C호실"),   // TODO: 실제 데이터로 교체
    );

    companion object {
        fun fromDisplayName(name: String): ContractData =
            entries.find { it.displayName == name } ?: UIJEONGBU

        fun displayNames() = entries.map { it.displayName }
    }
}

// ─── 장례식장별 빈소임대료 데이터 ─────────────────────────────────────────────
fun defaultRoomPriceItems(funeralHome: ContractData = ContractData.UIJEONGBU) = when (funeralHome) {
    ContractData.UIJEONGBU -> listOf(
        RoomPriceItem("201호실",       "56석",  "50평형",  "26,000원",  "624,000원"),
        RoomPriceItem("202호실",       "84석",  "65평형",  "41,000원",  "984,000원"),
        RoomPriceItem("203호실",       "80석",  "65평형",  "41,000원",  "984,000원"),
        RoomPriceItem("VIP호실",      "120석", "118평형",  "50,000원", "1,200,000원"),
        RoomPriceItem("VVIP호실",     "140석", "136평형",  "60,000원", "1,440,000원"),
        RoomPriceItem("VIP+VVIP호실", "270석", "270평형", "100,000원", "2,400,000원"),
    )
    ContractData.SEMIN -> listOf(
        // TODO: 실제 데이터로 교체
        RoomPriceItem("1호실", "50석", "45평형", "20,000원", "480,000원"),
    )
    ContractData.INCHEON -> listOf(
        // TODO: 실제 데이터로 교체
        RoomPriceItem("A호실", "60석", "55평형", "30,000원", "720,000원"),
    )
}

// ─── 부대시설 이용료 데이터 (호실 타입에 따라 오물수거료 단가 분기) ─────────────
fun defaultServiceItems(roomType: RoomType = RoomType.NORMAL) = listOf(
    ServiceItem("시신안치료",            "1일 / 120,000원",                     "일", unitPrice = 120000L),
    ServiceItem("수시초염료",            "150,000원 / 1회",                     "회", unitPrice = 150000L),
    ServiceItem("염습료",               "300,000원 / 1회",                     "회", unitPrice = 300000L),
    ServiceItem("시간 외/특수염습",      "300,000원 / 1회",                     "회", unitPrice = 300000L),
    ServiceItem("입관실사용료",          "300,000원 / 1회",                     "회", unitPrice = 300000L),
    ServiceItem("소독비",               "100,000원 / 1회",                     "회", unitPrice = 100000L),
    ServiceItem("감염성 폐기물(적출물)", "100,000원 / 1회",                     "회", unitPrice = 100000L),
    ServiceItem(
        title     = "오물수거료(1일)",
        price     = "VIP+VVIP 70,000원 / 일반실 50,000원",
        unitText  = "일",
        unitPrice = if (roomType == RoomType.VIPVVIP) 70000L else 50000L
    ),
)