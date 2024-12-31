package org.lamysia.christmasgrapes.di

import android.content.Context

actual class PlatformContext(val context: Context) {
    companion object {
        fun from(context: Context): PlatformContext = PlatformContext(context)
    }
}
