package org.lamysia.christmasgrapes.data.repository

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
                "May this year bring me closer to all my dreams.",
                "I finally got the call and landed my dream job at Apple's design team!",
                "After saving for three years, I bought my first apartment in the city center and hosted a housewarming party!",
                "I completed my first marathon in under 4 hours and my family was there cheering at the finish line!",
                "My food blog reached 100,000 subscribers and I got offered a cookbook deal!",
                "I mastered playing the guitar and performed on stage for the first time at my friend's wedding!",
                "My startup secured its first round of funding and we're expanding to international markets!",
                "I took that solo trip to Japan I've been planning for years and watched the cherry blossoms in Kyoto!",
                "After months of practicing, I finally learned to surf and caught my first perfect wave in Bali!",
                "I published my first novel and saw it on the shelves of my local bookstore!",
                "I reconnected with my childhood best friend after 15 years and it felt like no time had passed!",
                "I rescued a dog from the shelter and she's become my best companion on hiking trips!",
                "I finally renovated my kitchen with the marble countertops and island I've always wanted!",
                "I learned Italian and had my first real conversation with locals during my trip to Rome!",
                "My YouTube channel hit 1 million subscribers and I received my Gold Play Button!",
                "I saved enough to buy my dream car - a vintage Mustang - and took it for a road trip along the coast!",
                "I got accepted into the master's program at Harvard and moved to Boston!",
                "I cooked a full Thanksgiving dinner for my extended family and everyone asked for my recipes!",
                "I built my own vegetable garden and made my first salad completely from homegrown ingredients!",
                "I paid off all my student loans five years early and celebrated with a weekend getaway!",
                "I learned to make pottery and sold my first handmade mug collection at the local craft fair!",
                "I took my parents on their dream vacation to Paris and we had dinner at the Eiffel Tower!",
                "I finally perfected my grandmother's secret recipe and my family couldn't tell the difference!",
                "I got certified as a yoga instructor and taught my first class at the beachfront studio!",
                "I bought the beach house I've been dreaming of and woke up to ocean views on my birthday!"
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
