package org.lamysia.christmasgrapes.data.repository

import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.FilterOperator
import io.github.jan.supabase.postgrest.query.Order
import org.lamysia.christmasgrapes.data.SupabaseClient
import org.lamysia.christmasgrapes.model.Wish

class WishRepository {
    private val client = SupabaseClient.client

    suspend fun insertWish(text: String, isPremium: Boolean, assignedMonth: Int? = 1): Result<Wish> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        val wish = Wish(
            text = text,
            isPremium = isPremium,
            assignedMonth = assignedMonth,
            userId = currentUser?.id
        )
        client.postgrest["wishes"]
            .insert(wish)
            .decodeSingle<Wish>()
    }

    suspend fun getAllWishes(): Result<List<Wish>> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        client.postgrest["wishes"]
            .select {
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<Wish>()
    }

    suspend fun getFreeWishes(): Result<List<Wish>> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        client.postgrest["wishes"]
            .select {
                filter("is_premium", FilterOperator.EQ, false)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
            .decodeList<Wish>()
    }

    suspend fun deleteWish(id: Int): Result<Unit> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        client.postgrest["wishes"]
            .delete {
                filter("id", FilterOperator.EQ, id)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
    }

    suspend fun updateWishMonth(wishId: Int, month: Int): Result<Unit> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        client.postgrest["wishes"]
            .update(
                {
                    set("assigned_month", month)
                }
            ) {
                filter("id", FilterOperator.EQ, wishId)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
    }

    suspend fun toggleWishCompletion(wishId: Int, isCompleted: Boolean): Result<Unit> = runCatching {
        println("Repository: Toggling wish completion - wishId: $wishId, isCompleted: $isCompleted")
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        
        // Simple update query
        client.postgrest["wishes"]
            .update({
                set("is_completed", isCompleted)
            }) {
                eq("id", wishId)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
            .also { println("Repository: Update executed") }

        // Verify the update immediately
        val verifyWish = client.postgrest["wishes"]
            .select { 
                eq("id", wishId)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
            .decodeSingleOrNull<Wish>()
            .also { println("Repository: Verification wish - $it") }

        if (verifyWish?.isCompleted != isCompleted) {
            throw Exception("Failed to update wish completion status. Expected: $isCompleted, Got: ${verifyWish?.isCompleted}")
        }
    }.onFailure {
        println("Repository: Error toggling wish completion - ${it.message}")
        println("Repository: Error stack trace - ${it.stackTraceToString()}")
    }

    suspend fun getFreeWishCount(): Result<Int> = runCatching {
        val currentUser = client.gotrue.currentSessionOrNull()?.user
        client.postgrest["wishes"]
            .select(count = Count.EXACT) {
                filter("is_premium", FilterOperator.EQ, false)
                currentUser?.id?.let { userId ->
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }.let { result ->
                (result.count() ?: 0L).toInt()
            }
    }
}
