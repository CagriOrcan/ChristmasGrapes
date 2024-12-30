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
    private val wishes = listOf(
        "May all your dreams come true in the new year!",
        "Wishing you success and fulfillment in everything you do.",
        "May your path be bright and your journey be filled with joy.",
        "Here's to new beginnings and beautiful moments ahead.",
        "May this year bring you closer to all your dreams.",
        "Wishing you 12 months of success, 52 weeks of laughter, and 365 days of happiness!",
        "May you find happiness in every moment of the coming year.",
        "Here's to a year filled with new adventures and achievements.",
        "May your year be as bright as the stars and as sweet as grapes!",
        "Wishing you peace, love, and prosperity in the new year."
    )

    fun generateWish(): String {
        return wishes.random()
    }
}
