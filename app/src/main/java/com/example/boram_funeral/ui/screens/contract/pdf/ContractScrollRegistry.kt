package com.example.boram_funeral.ui.screens.contract.pdf

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 각 계약서 Step이 자신의 ScrollState를 등록해두면,
 * PDF 캡처 시 programmatic scroll로 전체 내용을 캡처할 수 있습니다.
 */
class ContractScrollRegistry {
    private val states = mutableMapOf<Int, ScrollState>()

    fun register(pageIndex: Int, scrollState: ScrollState) {
        states[pageIndex] = scrollState
    }

    operator fun get(pageIndex: Int): ScrollState? = states[pageIndex]
}

/** Step에서 자신의 ScrollState를 등록하는 람다 — (pageIndex, scrollState) */
val LocalScrollStateRegistrar = staticCompositionLocalOf<(Int, ScrollState) -> Unit> { { _, _ -> } }

/** HorizontalPager가 각 페이지에 주입하는 현재 페이지 번호 */
val LocalPageIndex = staticCompositionLocalOf { -1 }
