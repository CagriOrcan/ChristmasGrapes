/*
package org.lamysia.christmasgrapes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.Package
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lamysia.christmasgrapes.data.RevenueCatRepository
import org.lamysia.christmasgrapes.data.UserSubscriptionRepository

class PremiumViewModel(
    private val repository: RevenueCatRepository = RevenueCatRepository(),
    private val userSubscriptionRepository: UserSubscriptionRepository = UserSubscriptionRepository()
) : ViewModel() {
    private val _offerings = MutableStateFlow<Offerings?>(null)
    val offerings: StateFlow<Offerings?> = _offerings.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        checkPremiumStatus()
        loadOfferings()
    }

    private fun loadOfferings() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _offerings.value = repository.getOfferings()
            } finally {
                _loading.value = false
            }
        }
    }

    private fun checkPremiumStatus() {
        viewModelScope.launch {
            _isPremium.value = repository.checkPremiumAccess()
            // Sync with Supabase
            userSubscriptionRepository.syncSubscriptionWithRevenueCat()
        }
    }

    fun purchasePremium(packageToPurchase: Package) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.purchasePackage(packageToPurchase)
                checkPremiumStatus() // Check premium status and sync with Supabase
            } finally {
                _loading.value = false
            }
        }
    }
}*/
