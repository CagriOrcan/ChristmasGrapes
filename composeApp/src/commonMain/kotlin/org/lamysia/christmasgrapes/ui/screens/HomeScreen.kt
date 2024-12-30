package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.grapes
import christmasgrapes.composeapp.generated.resources.snowy
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(
    isPremium: Boolean = false,
    onUpgradeToPremium: () -> Unit = {},
    onGenerateWish: () -> Unit = {},
) {
    var isShaking by remember { mutableStateOf(false) }
    val shakeController = rememberInfiniteTransition()

    val shake by shakeController.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(isShaking) {
        if (isShaking) {
            delay(500)
            isShaking = false
            onGenerateWish()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = "Snowy Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Grapes Image with Shake Effect
        Image(
            painter = painterResource(Res.drawable.grapes),
            contentDescription = "Grapes",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isShaking = true
                }
                .graphicsLayer {
                    if (isShaking) {
                        rotationZ = shake * 5f // Adjust shake intensity
                        translationX = shake * 5f
                    }
                }
        )
    }
}

@Composable
fun HomeScreenPreview() {
    HomeScreen()
}