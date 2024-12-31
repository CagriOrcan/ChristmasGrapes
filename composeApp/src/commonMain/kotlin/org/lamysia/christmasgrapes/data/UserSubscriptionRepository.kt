package org.lamysia.christmasgrapes.data

import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class UserSubscription(
    val id: String,
    val customer_id: String? = null,
    val is_premium: Boolean = false,
    val subscription_status: String? = null,
    val subscription_expiry: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

class UserSubscriptionRepository {
    private val client = SupabaseClient.client
    private val revenueCatRepository = RevenueCatRepository()

    suspend fun getCurrentSession(): UserSession? {
        return client.gotrue.currentSessionOrNull()
    }

    suspend fun createOrUpdateSubscription(customerId: String, isPremium: Boolean, status: String) {
        val currentUser = getCurrentSession()?.user ?: return
        
        try {
            // Check if subscription exists
            val existingSubscription = client.postgrest["user_subscriptions"]
                .select { filter("id", FilterOperator.EQ, currentUser.id) }
                .decodeSingleOrNull<UserSubscription>()

            if (existingSubscription == null) {
                // Create new subscription
                client.postgrest["user_subscriptions"]
                    .insert(
                        UserSubscription(
                            id = currentUser.id,
                            customer_id = customerId,
                            is_premium = isPremium,
                            subscription_status = status,
                            updated_at = Clock.System.now().toString()
                        )
                    )
            } else {
                // Update existing subscription
                client.postgrest["user_subscriptions"]
                    .update(
                        {
                            set("customer_id", customerId)
                            set("is_premium", isPremium)
                            set("subscription_status", status)
                            set("updated_at", Clock.System.now().toString())
                        }
                    ) {
                        filter("id", FilterOperator.EQ, currentUser.id)
                    }
            }
        } catch (e: Exception) {
            println("Error updating subscription: ${e.message}")
            throw e
        }
    }

    suspend fun getUserSubscription(): UserSubscription? {
        val currentUser = getCurrentSession()?.user ?: return null
        
        return try {
            client.postgrest["user_subscriptions"]
                .select { filter("id", FilterOperator.EQ, currentUser.id) }
                .decodeSingleOrNull<UserSubscription>()
        } catch (e: Exception) {
            println("Error fetching subscription: ${e.message}")
            null
        }
    }

    suspend fun syncSubscriptionWithRevenueCat() {
        val isPremium = revenueCatRepository.checkPremiumAccess()
        val customerInfo = revenueCatRepository.getCustomerInfo()
        
        if (customerInfo != null) {
            createOrUpdateSubscription(
                customerId = customerInfo.originalAppUserId,
                isPremium = isPremium,
                status = if (isPremium) "active" else "inactive"
            )
        }
    }
} 