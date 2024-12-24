package org.lamysia.christmasgrapes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.ui.MainScreen
import org.lamysia.christmasgrapes.ui.theme.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun App() {
    AppTheme {
        MainScreen()
    }
}