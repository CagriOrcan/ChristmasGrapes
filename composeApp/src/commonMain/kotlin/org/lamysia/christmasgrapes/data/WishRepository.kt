package org.lamysia.christmasgrapes.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lamysia.christmasgrapes.model.Wish

class WishRepository {
    private val _wishes = MutableStateFlow<List<Wish>>(emptyList())
    private var _isPremium = MutableStateFlow(false)

    init {
        // Initialize with 12 empty grapes
        _wishes.value = List(12) { index ->
            Wish(
                id = index,
                text = "",
                isLocked = index >= 3,
                hasWish = false
            )
        }
    }

    fun getWishes(): StateFlow<List<Wish>> = _wishes.asStateFlow()
    fun isPremium(): StateFlow<Boolean> = _isPremium.asStateFlow()

    fun updateWish(id: Int, text: String) {
        _wishes.value = _wishes.value.map {
            if (it.id == id) it.copy(text = text, hasWish = text.isNotEmpty()) else it
        }
    }

    fun upgradeToPermium() {
        _isPremium.value = true
        _wishes.value = _wishes.value.map { it.copy(isLocked = false) }
    }
}