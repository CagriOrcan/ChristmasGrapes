package org.lamysia.christmasgrapes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.di.LocalPlatformContext
import org.lamysia.christmasgrapes.di.PlatformContext
import org.lamysia.christmasgrapes.ui.utils.ShareUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalPlatformContext provides PlatformContext.from(this)
            ) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

val LocalShareUtil = staticCompositionLocalOf<ShareUtil> {
    error("ShareUtil not provided")
}