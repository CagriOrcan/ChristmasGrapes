/*
package org.lamysia.christmasgrapes.data

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.revenuecat.purchases.kmp.models.Offerings
import com.revenuecat.purchases.kmp.models.Package

class RevenueCatRepository {

    init {
        Purchases.configure(
            PurchasesConfiguration.Builder(ApiConfig.REVENUECAT_API_KEY)
                .build()
        )
    }

    suspend fun getOfferings(): Offerings? {
        return try {
            var offerings: Offerings? = null
            Purchases.sharedInstance.getOfferings(
                onError = { error ->
                    println("Error fetching offerings: ${error.message}")
                },
                onSuccess = { result ->
                    offerings = result
                }
            )
            offerings
        } catch (e: Exception) {
            println("Exception fetching offerings: ${e.message}")
            null
        }
    }

    suspend fun purchasePackage(packageToPurchase: Package) {
        try {
            Purchases.sharedInstance.purchase(
                packageToPurchase = packageToPurchase,
                onError = { error, bool ->
                    println("Error purchasing package: ${error.message}")
                },
                onSuccess = { transaction, customerInfo ->
                    println("Purchase successful: ${transaction.transactionId}")
                }
            )
        } catch (e: Exception) {
            println("Exception during purchase: ${e.message}")
        }
    }

    suspend fun checkPremiumAccess(): Boolean {
        return try {
            var isPremium = false
            Purchases.sharedInstance.getCustomerInfo(
                onError = { error ->
                    println("Error checking premium access: ${error.message}")
                },
                onSuccess = { customerInfo ->
                    isPremium = customerInfo.entitlements.all["premium"]?.isActive == true
                }
            )
            isPremium
        } catch (e: Exception) {
            println("Exception checking premium access: ${e.message}")
            false
        }
    }

    suspend fun getCustomerInfo(): com.revenuecat.purchases.kmp.CustomerInfo? {
        return try {
            var customerInfo: com.revenuecat.purchases.kmp.CustomerInfo? = null
            Purchases.sharedInstance.getCustomerInfo(
                onError = { error ->
                    println("Error fetching customer info: ${error.message}")
                },
                onSuccess = { info ->
                    customerInfo = info
                }
            )
            customerInfo
        } catch (e: Exception) {
            println("Exception fetching customer info: ${e.message}")
            null
        }
    }
}*/
