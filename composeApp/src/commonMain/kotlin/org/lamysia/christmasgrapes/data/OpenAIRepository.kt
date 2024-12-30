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

    private val apiKey = "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"
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    suspend fun generateWish(): String = withContext(Dispatchers.IO) {
        try {
            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    Message("system", "You are wish generator.You know the tradition of eating 12 grapes on New Year's Eve. You make a wish for each month and eat grapes, and you will create a wish for people for each grape. And you will wish for it firsthand as if you were making a wish yourself. you generate one wish in each time. Wish will be one word"),
                    Message("user", "Generate a wish for me")
                ),
                max_tokens = 100,
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
                "May all your dreams come true in the new year!",
                "Wishing you success and fulfillment in everything you do.",
                "May your path be bright and your journey be filled with joy.",
                "Here's to new beginnings and beautiful moments ahead.",
                "May this year bring you closer to all your dreams."
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
