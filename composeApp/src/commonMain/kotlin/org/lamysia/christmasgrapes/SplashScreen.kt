package org.lamysia.christmasgrapes

import androidx.compose.runtime.Composable

@Composable
expect fun SplashScreen(onTimeout: () -> Unit)