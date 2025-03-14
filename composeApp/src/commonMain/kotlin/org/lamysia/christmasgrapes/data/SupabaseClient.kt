package org.lamysia.christmasgrapes.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import org.lamysia.christmasgrapes.config.ApiConfig

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = ApiConfig.SUPABASE_URL_KEY,
        supabaseKey = ApiConfig.SUPABASE_API_KEY
    ) {
        install(Postgrest)
        install(GoTrue)
    }
}
