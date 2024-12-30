package org.lamysia.christmasgrapes.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://gacycidqmxmajfiqwhzk.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdhY3ljaWRxbXhtYWpmaXF3aHprIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU1MjIzNjQsImV4cCI6MjA1MTA5ODM2NH0.7MxFnTs61D3LSGjAtYgP_iBdwUsPLIfARzgTaibia0k"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}
