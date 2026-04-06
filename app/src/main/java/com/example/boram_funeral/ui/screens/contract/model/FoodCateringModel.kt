package com.example.boram_funeral.ui.screens.contract.model

data class FoodServiceItem(
    val category: String? = null,
    val name: String,
    val origin: String = "",
    val unit: String,
    val price: String,
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
    val isHeaderStyle: Boolean = false
)

data class PriceDetail(
    val level: String,
    val price: String
)

fun defaultFoodItems() = listOf(
    FoodServiceItem(category = "밥",   name = "밥(국내산)",                          unit = "50인분(5kg)",  price = "60,000",  origin = "쌀(국내산)"),
    FoodServiceItem(category = "국",   name = "얼갈이된장국",                         unit = "50인분",       price = "175,000", origin = "얼갈이(국내산)"),
    FoodServiceItem(category = "국",   name = "북어국",                              unit = "50인분",       price = "175,000", origin = "북어(러시아)"),
    FoodServiceItem(category = "국",   name = "소고기 무국",                          unit = "50인분",       price = "190,000", origin = "소고기(호주산)"),
    FoodServiceItem(category = "국",   name = "육개장",                              unit = "50인분",       price = "190,000", origin = "소고기(호주), 고추가루,고사리(중국)"),
    FoodServiceItem(category = "무침", name = "가오리 무침",                          unit = "(3kg)",        price = "150,000", origin = "가오리(남미)"),
    FoodServiceItem(category = "찜",   name = "코다리찜",                             unit = "(4kg)",        price = "110,000", origin = "북어(러시아)"),
    FoodServiceItem(category = "강정", name = "명태 강정",                            unit = "(2kg)",        price = "130,000", origin = "북어(러시아)"),
    FoodServiceItem(category = "반찬류", name = "모듬전 (동태전,부추전,해물전)",          unit = "(4kg)",        price = "130,000", origin = "동태(러시아), 야채(국내산,수입산), 해물(수입산)"),
    FoodServiceItem(category = "반찬류", name = "고추멸치볶음",                        unit = "(2kg)",        price = "100,000", origin = "가이리멸치(국내산)"),
    FoodServiceItem(category = "반찬류", name = "수육",                              unit = "(8kg)(40인분)", price = "360,000", origin = "돼지고기(국내산)"),
    FoodServiceItem(category = "반찬류", name = "매실장아찌",                          unit = "(2kg)",        price = "80,000",  origin = "매실(국내산)"),
    FoodServiceItem(category = "반찬류", name = "콩나물무침",                          unit = "(3kg)",        price = "50,000",  origin = "콩(수입산)"),
    FoodServiceItem(category = "반찬류", name = "김치",                              unit = "(5kg)",        price = "60,000",  origin = "배추(국내산), 고추가루(국내산)"),
    FoodServiceItem(category = "반찬류", name = "샐러드",                             unit = "(3kg)",        price = "80,000",  origin = "사과(국내산)"),
    FoodServiceItem(category = "반찬류", name = "새우젓",                             unit = "(1kg)",        price = "20,000",  origin = "새우(중국산)"),
)

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
        category = "상 식",
        flatPrice = "50,000 x (      )회"
    ),
    FoodCategoryItem(
        category = "노 제",
        priceLevels = listOf(PriceDetail("上", "650,000"), PriceDetail("中", "450,000"))
    ),
    FoodCategoryItem(
        category = "위령제,산신제",
        flatPrice = "기본 200,000"
    )
)