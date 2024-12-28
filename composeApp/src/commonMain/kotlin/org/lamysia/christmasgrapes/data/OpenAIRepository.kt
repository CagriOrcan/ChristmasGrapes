package org.lamysia.christmasgrapes.data

/*
class OpenAIRepository {
    private val openAI = OpenAI(token = "your-api-key")

    suspend fun generateWish(): String {
        val request = CompletionRequest(
            model = ModelId("gpt-3.5-turbo-instruct"),
            prompt = "Generate a positive, inspiring new year wish in one sentence:",
            maxTokens = 50
        )

        val completion = openAI.completion(request)
        return completion.choices.first().text.trim()
    }
}*/

class OpenAIRepository {
    // Şimdilik sabit dilekler döndürelim
    private val wishes = listOf(
        "May all your dreams come true in the new year!",
        "Wishing you joy, peace, and prosperity in the coming year.",
        "May this year bring you endless opportunities and happiness.",
        "Here's to new adventures and beautiful moments ahead!",
        "May your year be filled with magical moments and wonderful surprises.",
        "Wishing you success and fulfillment in everything you do.",
        "May this year bring you closer to all your dreams.",
        "Here's to a year of growth, learning, and achievement.",
        "May your path be bright and your burdens light.",
        "Wishing you 12 months of success, 52 weeks of laughter, and 365 days of happiness!"
    )

    suspend fun generateWish(): String {
        return wishes.random()
    }
}
