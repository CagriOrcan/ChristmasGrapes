package org.lamysia.christmasgrapes.ui.utils

import org.lamysia.christmasgrapes.di.PlatformContext
import org.lamysia.christmasgrapes.model.Wish

actual class ShareUtil actual constructor(private val platformContext: PlatformContext?) {
    actual suspend fun shareWish(wishText: String) {
        // iOS implementasyonu
    }

    actual suspend fun captureAndSharePostcard(wish: Wish) {
        // iOS implementasyonu
    }
}