package com.example.boram_funeral.ui.screens.counsel.model

data class ProductItem(
    val id: Int,
    val name: String,
    val description: String = "",
    val price: String = "",
    val imageResId: Int? = null,
)

// CustomSubTabControl 탭 순서와 동일 (이용계약서 제외, index 1부터 매핑)
val productCategories = listOf(
    "관", "유골함", "각인", "수의", "멧베", "제단", "헌화꽃", "장의차량", "고깔", "양복", "개량", "셔츠", "타이"
)

fun defaultProductMap(): Map<String, List<ProductItem>> = mapOf(
    "관" to listOf(
        ProductItem(1,  "1.7 오동화장관",   price = "400,000원"),
        ProductItem(2,  "2.5 오동맞춤관",   price = "600,000원"),
        ProductItem(3,  "4.2 오동매장관",   price = "480,000원"),
        ProductItem(4,  "4.2 솔송매장관",   price = "800,000원"),
        ProductItem(5,  "4.2 향매장관",     price = "1,800,000원"),
    ),
    "유골함" to listOf(
        ProductItem(10, "유골함",           price = ""),
        ProductItem(11, "각인비",           price = ""),
        ProductItem(12, "전사비",           price = ""),
    ),
    "각인" to listOf(
        ProductItem(20, "각인 기본",        price = ""),
        ProductItem(21, "각인 프리미엄",    price = ""),
    ),
    "수의" to listOf(
        ProductItem(30, "수의",             price = ""),
    ),
    "멧베" to listOf(
        ProductItem(40, "멧베(명품)",       price = "300,000원"),
        ProductItem(41, "멧베(특명품)",     price = "400,000원"),
    ),
    "제단"    to emptyList(),
    "헌화꽃"  to emptyList(),
    "장의차량" to listOf(
        ProductItem(70, "리무진, 버스",     price = "1,500,000원"),
        ProductItem(71, "이송비",           price = ""),
    ),
    "고깔" to listOf(
        ProductItem(80, "고깔(명품)",       price = "200,000원"),
    ),
    "양복" to emptyList(),
    "개량" to emptyList(),
    "셔츠" to emptyList(),
    "타이" to emptyList(),
)