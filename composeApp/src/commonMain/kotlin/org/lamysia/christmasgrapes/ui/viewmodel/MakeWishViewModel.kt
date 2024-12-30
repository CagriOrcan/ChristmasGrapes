package org.lamysia.christmasgrapes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.lamysia.christmasgrapes.data.WishRepository
import org.lamysia.christmasgrapes.model.Wish

class MakeWishViewModel: ViewModel() {
    private val repository = WishRepository()

    private val _wishes = MutableStateFlow<List<Wish>>(emptyList())
    val wishes: StateFlow<List<Wish>> = _wishes.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadWishes()
    }

    private fun loadWishes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.getAllWishes()
                    .onSuccess {
                        _wishes.value = it
                        _error.value = null
                    }
                    .onFailure {
                        _error.value = it.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun addWish(text: String, isPremium: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.insertWish(text, isPremium)
                    .onSuccess {
                        loadWishes()
                        _error.value = null
                    }
                    .onFailure {
                        _error.value = it.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteWish(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.deleteWish(id)
                    .onSuccess {
                        loadWishes()
                        _error.value = null
                    }
                    .onFailure {
                        _error.value = it.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    // State'i UI'da yönetmek için bir Flow olarak dönelim
    val canAddFreeWish = flow {
        repository.getFreeWishCount()
            .onSuccess { count ->
                emit(count < 3)
            }
            .onFailure {
                _error.value = it.message
                emit(false)
            }
    }
}