package org.lamysia.christmasgrapes.model

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wish(
    val id: Int? = null,
    val text: String,
    @SerialName("created_at")
    val createdAt: String = Clock.System.now().toString(),
    @SerialName("is_premium")
    val isPremium: Boolean = true,
    val hasWish: Boolean = text.isNotEmpty(),
    @SerialName("assigned_month")
    val assignedMonth: Int? = 1,
    @SerialName("is_completed")
    val isCompleted: Boolean = false
)