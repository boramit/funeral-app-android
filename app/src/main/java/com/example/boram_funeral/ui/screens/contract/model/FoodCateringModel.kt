package com.example.boram_funeral.ui.screens.contract.model

// 단위별 가격 옵션 — API 연동 전 임시 목업 데이터
data class FoodItemOption(
    val unit: String,
    val price: String,
)

data class FoodServiceItem(
    val category: String? = null,
    val name: String,
    val origin: String = "",
    val unit: String,
    val price: String,
    val options: List<FoodItemOption> = emptyList(), // 단위별 가격 옵션 (API 연동 예정)
    val isHeader: Boolean = false,
    val isYellowHeader: Boolean = false,
    val isReadOnly: Boolean = false,
    val remarks: String = ""
)

data class FoodCategoryItem(
    val category: String,
    val priceLevels: List<PriceDetail> = emptyList(),
    val flatPrice: String? = null,
    val totalAmount: String = "",
    val isHeaderStyle: Boolean = false,
    val selectedLevel: String? = null
)

data class PriceDetail(
    val level: String,
    val price: String
)

// ─── 공통 품목 ────────────────────────────────────────────────────────────────

private val 밥 = FoodServiceItem(
    category = "밥", name = "밥(국내산)", origin = "쌀(국내산)",
    unit = "50인분(5kg)", price = "60,000",
    options = listOf(
        FoodItemOption("50인분(5kg)",   "60,000"),
        FoodItemOption("100인분(10kg)", "120,000"),
        FoodItemOption("150인분(15kg)", "180,000"),
    ),
)

private val 얼갈이된장국 = FoodServiceItem(
    category = "국", name = "얼갈이된장국", origin = "얼갈이(국내산)",
    unit = "50인분", price = "175,000",
    options = listOf(
        FoodItemOption("50인분",  "175,000"),
        FoodItemOption("100인분", "350,000"),
    ),
)

private val 시락국 = FoodServiceItem(
    category = "국", name = "시락국", origin = "시래기(국내산)",
    unit = "50인분", price = "160,000",
    options = listOf(
        FoodItemOption("50인분",  "160,000"),
        FoodItemOption("100인분", "320,000"),
    ),
)

private val 북어국 = FoodServiceItem(
    category = "국", name = "북어국", origin = "북어(러시아)",
    unit = "50인분", price = "175,000",
    options = listOf(
        FoodItemOption("50인분",  "175,000"),
        FoodItemOption("100인분", "350,000"),
    ),
)

private val 소고기무국 = FoodServiceItem(
    category = "국", name = "소고기 무국", origin = "소고기(호주산)",
    unit = "50인분", price = "190,000",
    options = listOf(
        FoodItemOption("50인분",  "190,000"),
        FoodItemOption("100인분", "380,000"),
    ),
)

private val 육개장 = FoodServiceItem(
    category = "국", name = "육개장", origin = "소고기(호주), 고추가루,고사리(중국)",
    unit = "50인분", price = "190,000",
    options = listOf(
        FoodItemOption("50인분",  "190,000"),
        FoodItemOption("100인분", "380,000"),
    ),
)

private val 공통반찬 = listOf(
    FoodServiceItem(
        category = "무침", name = "가오리 무침", origin = "가오리(남미)",
        unit = "(3kg)", price = "150,000",
        options = listOf(FoodItemOption("(3kg)", "150,000"), FoodItemOption("(6kg)", "300,000")),
    ),
    FoodServiceItem(
        category = "찜", name = "코다리찜", origin = "북어(러시아)",
        unit = "(4kg)", price = "110,000",
        options = listOf(FoodItemOption("(4kg)", "110,000"), FoodItemOption("(8kg)", "220,000")),
    ),
    FoodServiceItem(
        category = "강정", name = "명태 강정", origin = "북어(러시아)",
        unit = "(2kg)", price = "130,000",
        options = listOf(FoodItemOption("(2kg)", "130,000"), FoodItemOption("(4kg)", "260,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "모듬전 (동태전,부추전,해물전)", origin = "동태(러시아), 야채(국내산,수입산), 해물(수입산)",
        unit = "(4kg)", price = "130,000",
        options = listOf(FoodItemOption("(4kg)", "130,000"), FoodItemOption("(8kg)", "260,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "고추멸치볶음", origin = "가이리멸치(국내산)",
        unit = "(2kg)", price = "100,000",
        options = listOf(FoodItemOption("(2kg)", "100,000"), FoodItemOption("(4kg)", "200,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "수육", origin = "돼지고기(국내산)",
        unit = "(8kg)(40인분)", price = "360,000",
        options = listOf(FoodItemOption("(8kg)(40인분)", "360,000"), FoodItemOption("(16kg)(80인분)", "720,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "매실장아찌", origin = "매실(국내산)",
        unit = "(2kg)", price = "80,000",
        options = listOf(FoodItemOption("(2kg)", "80,000"), FoodItemOption("(4kg)", "160,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "콩나물무침", origin = "콩(수입산)",
        unit = "(3kg)", price = "50,000",
        options = listOf(FoodItemOption("(3kg)", "50,000"), FoodItemOption("(6kg)", "100,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "김치", origin = "배추(국내산), 고추가루(국내산)",
        unit = "(5kg)", price = "60,000",
        options = listOf(FoodItemOption("(5kg)", "60,000"), FoodItemOption("(10kg)", "120,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "샐러드", origin = "사과(국내산)",
        unit = "(3kg)", price = "80,000",
        options = listOf(FoodItemOption("(3kg)", "80,000"), FoodItemOption("(6kg)", "160,000")),
    ),
    FoodServiceItem(
        category = "반찬류", name = "새우젓", origin = "새우(중국산)",
        unit = "(1kg)", price = "20,000",
        options = listOf(FoodItemOption("(1kg)", "20,000"), FoodItemOption("(2kg)", "40,000")),
    ),
)

// ─── 장례식장별 음식 목록 ──────────────────────────────────────────────────────
// 추후 API 연동 시 이 함수를 repository 호출로 교체

fun defaultFoodItems(funeralHome: ContractData = ContractData.UIJEONGBU): List<FoodServiceItem> {
    val 국목록 = when (funeralHome) {
        ContractData.SEMIN -> listOf(시락국, 북어국, 소고기무국, 육개장)   // 세민에스: 시락국 사용
        else               -> listOf(얼갈이된장국, 북어국, 소고기무국, 육개장)
    }

    return buildList {
        add(밥)
        addAll(국목록)
        addAll(공통반찬)
    }
}

fun defaultFoodCategoryItems() = listOf(
    FoodCategoryItem(
        category = "초배상",
        priceLevels = listOf(PriceDetail("上", "400,000"), PriceDetail("中", "300,000"))
    ),
    FoodCategoryItem(
        category = "성복제",
        priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
    ),
    FoodCategoryItem(
        category = "발인제",
        priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
    ),
    FoodCategoryItem(
        category = "상식",
        flatPrice = "50,000 x (      )회"
    ),
    FoodCategoryItem(
        category = "노제",
        priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
    ),
    FoodCategoryItem(
        category = "위령제, 산신제",
        flatPrice = "기본 200,000"
    )
)
