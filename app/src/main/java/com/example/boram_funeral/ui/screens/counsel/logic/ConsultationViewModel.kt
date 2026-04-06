package com.example.boram_funeral.ui.screens.counsel.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boram_funeral.ui.screens.counsel.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConsultationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConsultationUiState())
    val uiState = _uiState.asStateFlow()

    // ── 탭 ────────────────────────────────────────────────────────────────────

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index, selectedProduct = null) }
    }

    // ── 모달 열기 / 닫기 ──────────────────────────────────────────────────────

    fun openProduct(product: ProductItem) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    /** 취소: 모달만 닫고 선택 상태는 변경하지 않음 */
    fun cancelProduct() {
        _uiState.update { it.copy(selectedProduct = null) }
    }

    /** 확인: 카테고리당 1개만 선택 가능. 이미 선택된 경우 해제. 모달 닫기 */
    fun confirmProduct(product: ProductItem) {
        _uiState.update { state ->
            val newIds = if (product.id in state.selectedProductIds) {
                state.selectedProductIds - product.id
            } else {
                val category = state.products.entries
                    .firstOrNull { (_, items) -> items.any { it.id == product.id } }
                    ?.key
                val sameCategory = if (category != null) {
                    state.products[category]?.map { it.id }?.toSet() ?: emptySet()
                } else emptySet()
                (state.selectedProductIds - sameCategory) + product.id
            }
            state.copy(selectedProductIds = newIds, selectedProduct = null)
        }
    }

    /** 목록에서 직접 선택 해제 */
    fun removeProduct(productId: Int) {
        _uiState.update { it.copy(selectedProductIds = it.selectedProductIds - productId) }
    }

    // ── 저장 ──────────────────────────────────────────────────────────────────

    /** 확인된 상품 전체를 저장. 실제 서버 전송은 repository 연결 후 추가 */
    fun saveSelectedProducts(onComplete: (List<ProductItem>) -> Unit) {
        val confirmed = _uiState.value.confirmedProducts
        Log.d("ConsultationVM", "저장할 상품 ${confirmed.size}개:")
        confirmed.forEach { Log.d("ConsultationVM", "  - ${it.name} / ${it.price}") }
        // TODO: repository.saveProducts(confirmed)
        onComplete(confirmed)
    }

    fun clearSelectedProducts() {
        _uiState.update { it.copy(selectedProductIds = emptySet()) }
    }
}