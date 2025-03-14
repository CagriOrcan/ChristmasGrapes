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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SparkleAnimation() {
    val particles = remember { mutableStateListOf<SparkleParticle>() }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            if (particles.size < 20) {
                particles.add(
                    SparkleParticle(
                        x = Random.nextFloat(),
                        y = Random.nextFloat(),
                        alpha = 1f,
                        scale = Random.nextFloat() * 0.5f + 0.5f
                    )
                )
            }
            particles.forEach { it.update() }
            particles.removeAll { it.alpha <= 0f }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = Color(0xFFFFD700),
                radius = 8.dp.toPx() * particle.scale,
                center = Offset(
                    x = particle.x * size.width,
                    y = particle.y * size.height
                ),
                alpha = particle.alpha
            )


            for (i in 0..3) {
                val angle = i * PI / 2
                drawLine(
                    color = Color(0xFFFFD700),
                    start = Offset(
                        x = particle.x * size.width + cos(angle).toFloat() * 10 * particle.scale,
                        y = particle.y * size.height + sin(angle).toFloat() * 10 * particle.scale
                    ),
                    end = Offset(
                        x = particle.x * size.width + cos(angle).toFloat() * 20 * particle.scale,
                        y = particle.y * size.height + sin(angle).toFloat() * 20 * particle.scale
                    ),
                    alpha = particle.alpha
                )
            }
        }
    }
}

data class SparkleParticle(
    val x: Float,
    val y: Float,
    var alpha: Float,
    val scale: Float
) {
    fun update() {
        alpha -= 0.02f
    }
}