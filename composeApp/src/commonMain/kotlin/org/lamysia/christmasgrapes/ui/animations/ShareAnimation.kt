package org.lamysia.christmasgrapes.ui.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed class ShareAnimationState {
    object Snow : ShareAnimationState()
    object Sparkle : ShareAnimationState()
    object Fireworks : ShareAnimationState()
    object None : ShareAnimationState()
}

@Composable
fun ShareAnimationEffect(
    state: ShareAnimationState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (state) {
            ShareAnimationState.Snow -> SnowAnimation()
            ShareAnimationState.Sparkle -> SparkleAnimation()
            ShareAnimationState.Fireworks -> FireworksAnimation()
            ShareAnimationState.None -> Unit
        }
    }
}