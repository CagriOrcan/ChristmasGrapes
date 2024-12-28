package org.lamysia.christmasgrapes.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MakeWishViewModel {
    private val _wishCount = MutableStateFlow(0)
    val wishCount: StateFlow<Int> = _wishCount.asStateFlow()

    val hasReachedFreeLimit: Boolean
        get() = _wishCount.value >= 3

    fun incrementWishCount() {
        _wishCount.value++
    }

    fun resetWishCount() {
        _wishCount.value = 0
    }
}