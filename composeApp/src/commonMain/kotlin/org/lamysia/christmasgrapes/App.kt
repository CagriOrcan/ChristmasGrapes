package org.lamysia.christmasgrapes

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.ui.MainScreen
import org.lamysia.christmasgrapes.ui.theme.AppTheme

@Preview
@Composable
fun App() {
    AppTheme {
        MainScreen()
    }
}