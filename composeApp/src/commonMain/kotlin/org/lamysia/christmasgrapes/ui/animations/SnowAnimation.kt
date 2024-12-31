package org.lamysia.christmasgrapes.ui.animations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SnowAnimation() {
    val particles = remember { mutableStateListOf<SnowParticle>() }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            if (particles.size < 50) {
                particles.add(
                    SnowParticle(
                        x = Random.nextFloat(),
                        y = -0.1f,
                        scale = Random.nextFloat() * 0.5f + 0.5f,
                        speed = Random.nextFloat() * 0.001f + 0.001f
                    )
                )
            }
            particles.forEach { it.update() }
            particles.removeAll { it.y > 1.1f }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx() * particle.scale,
                center = Offset(
                    x = particle.x * size.width,
                    y = particle.y * size.height
                ),
                alpha = 0.8f
            )
        }
    }
}

data class SnowParticle(
    var x: Float,
    var y: Float,
    val scale: Float,
    val speed: Float
) {
    fun update() {
        y += speed
        x += sin(y * 10) * 0.01f
    }
}