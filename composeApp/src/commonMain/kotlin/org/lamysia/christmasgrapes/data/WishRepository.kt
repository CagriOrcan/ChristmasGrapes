package org.lamysia.christmasgrapes.data

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.FilterOperator
import io.github.jan.supabase.postgrest.query.Order
import org.lamysia.christmasgrapes.model.Wish

class WishRepository {
    private val client = SupabaseClient.client

    suspend fun insertWish(text: String, isPremium: Boolean): Result<Wish> = runCatching {
        val wish = Wish(
            text = text,
            isPremium = isPremium
        )
        client.postgrest["wishes"]
            .insert(wish)
            .decodeSingle<Wish>()
    }

    suspend fun getAllWishes(): Result<List<Wish>> = runCatching {
        client.postgrest["wishes"]
            .select{
                order("created_at", Order.DESCENDING)
            }
            .decodeList<Wish>()
    }

    suspend fun getFreeWishes(): Result<List<Wish>> = runCatching {
        client.postgrest["wishes"]
            .select {
                filter("is_premium", FilterOperator.EQ, false)
            }
            .decodeList<Wish>()
    }

    suspend fun deleteWish(id: Int): Result<Unit> = runCatching {
        client.postgrest["wishes"]
            .delete {
                filter("id", FilterOperator.EQ, id)
            }
    }

    suspend fun getFreeWishCount(): Result<Int> = runCatching {
        client.postgrest["wishes"]
            .select(count = Count.EXACT) {
                filter("is_premium", FilterOperator.EQ, false)
            }.let { result ->
                (result.count() ?: 0L).toInt()
            }
    }
}
