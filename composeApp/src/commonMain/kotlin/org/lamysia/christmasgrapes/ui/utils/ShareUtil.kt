package org.lamysia.christmasgrapes.ui.utils

import org.lamysia.christmasgrapes.di.PlatformContext
import org.lamysia.christmasgrapes.model.Wish

expect class ShareUtil(platformContext: PlatformContext?) {
    suspend fun shareWish(wishText: String)
    suspend fun captureAndSharePostcard(wish: Wish)
}
