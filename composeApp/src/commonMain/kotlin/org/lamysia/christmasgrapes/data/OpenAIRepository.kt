package org.lamysia.christmasgrapes.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.lamysia.christmasgrapes.config.ApiConfig
import kotlin.coroutines.cancellation.CancellationException

class OpenAIRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }

    }

    fun close() {
        client.close()
    }

    private val apiKey = ApiConfig.OPENAI_API_KEY
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    suspend fun generateWish(): String = withContext(Dispatchers.IO) {
        try {
            val request = OpenAIRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    Message("system", "You are a wish generator for the New Year's Eve 12 grapes tradition. Generate short, meaningful first-person wishes in English (1-2 sentences max) based on what people commonly desire in recent years. Focus on analyses of the what people needs. Write them in past tense as if they've already come true. IMPORTANT: Each wish must be completely different from others - avoid similar themes, verbs, or subjects in consecutive wishes. For example, if one wish was about career, the next should be about a completely different aspect of life. Examples of diverse wishes: 'I successfully launched my dream tech startup' (career) -> 'I found inner peace through daily meditation' (wellness) -> 'I bought my beachfront home in my favorite coastal town' (lifestyle). Keep each wish brief, contemporary, and ensure maximum variety."),
                    Message("user", "Generate a unique wish that is completely different from any previous wishes in both theme and content")
                ),
                max_tokens = 1000,
                temperature = 0.7
            )

            client.post(baseUrl) {
                headers {
                    append("Authorization", "Bearer $apiKey")
                    append("Content-Type", "application/json")
                }
                setBody(request)
            }.let { response ->
                if (response.status.isSuccess()) {
                    val jsonResponse = Json.decodeFromString<JsonObject>(response.bodyAsText())
                    jsonResponse["choices"]?.jsonArray?.get(0)?.jsonObject
                        ?.get("message")?.jsonObject
                        ?.get("content")?.jsonPrimitive?.content?.trim()
                        ?: throw Exception("Invalid response format")
                } else {
                    throw Exception("API call failed: ${response.status}")
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(
                "May all my dreams come true in the new year!",
                "Wishing you success and fulfillment in everything",
                "May my path be bright and your journey be filled with joy.",
                "Here's to new beginnings and beautiful moments ahead.",
                "May this year bring me closer to all my dreams."
            ).random()
        }
    }
}

@kotlinx.serialization.Serializable
data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int,
    val temperature: Double
)

@kotlinx.serialization.Serializable
data class Message(
    val role: String,
    val content: String
)
