package org.lamysia.christmasgrapes.model

data class Wish(
    val id: Int,
    val text: String,
    val isLocked: Boolean = false,
    val dateCreated: String = getCurrentDate(),
    val hasWish: Boolean = false
)

private fun getCurrentDate(): String {
    // Platform spesifik tarih formatlaması için daha sonra expect/actual kullanacağız
    return "2024-01-01" // Şimdilik sabit bir değer
}