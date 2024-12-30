package org.lamysia.christmasgrapes.data

import io.ktor.client.HttpClient
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

class OpenAIRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private val apiKey = "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"
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    suspend fun generateWish(): String = withContext(Dispatchers.IO) {
        try {
            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    Message("system", "You are wish generator."),
                    Message("user", "You know the tradition of eating 12 grapes on New Year's Eve. You make a wish for each month and eat grapes, and you will create a wish for people for each grape. And you will wish for it firsthand as if you were making a wish yourself. you generate one wish in each time. ")
                ),
                max_tokens = 100,
                temperature = 0.7
            )

            val response = client.post(baseUrl) {
                headers {
                    append("Authorization", "Bearer $apiKey")
                }
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val jsonResponse = Json.decodeFromString<JsonObject>(response.bodyAsText())
                val content = jsonResponse["choices"]?.jsonArray?.get(0)?.jsonObject
                    ?.get("message")?.jsonObject
                    ?.get("content")?.jsonPrimitive?.content
                content?.trim() ?: throw Exception("Invalid response format")
            } else {
                throw Exception("API call failed: ${response.status}")
            }
        } catch (e: Exception) {
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
