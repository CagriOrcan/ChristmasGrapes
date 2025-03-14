package org.lamysia.christmasgrapes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lamysia.christmasgrapes.data.repository.OpenAIRepository
import org.lamysia.christmasgrapes.data.repository.WishRepository
import org.lamysia.christmasgrapes.model.Wish

class MakeWishViewModel: ViewModel() {
    private val repository = WishRepository()
    private val openAIRepository = OpenAIRepository()

    private val _wishes = MutableStateFlow<List<Wish>>(emptyList())
    val wishes: StateFlow<List<Wish>> = _wishes.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadWishes()
    }

    override fun onCleared() {
        super.onCleared()
        openAIRepository.close()
    }

    suspend fun generateWish(): String {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                _loading.value = true
                openAIRepository.generateWish()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadWishes() {
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

    fun addWish(text: String, isPremium: Boolean, assignedMonth: Int? = 1) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.insertWish(text, isPremium, assignedMonth)
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

    fun updateWishMonth(wishId: Int, month: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.updateWishMonth(wishId, month)
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

    fun toggleWishCompletion(wishId: Int, isCompleted: Boolean) {
        println("ViewModel: Toggling wish completion - wishId: $wishId, isCompleted: $isCompleted")
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.toggleWishCompletion(wishId, isCompleted)
                    .onSuccess {
                        println("ViewModel: Wish completion toggle successful, reloading wishes")
                        loadWishes()
                        _error.value = null
                    }
                    .onFailure { error ->
                        println("ViewModel: Error toggling wish completion - ${error.message}")
                        _error.value = error.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

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