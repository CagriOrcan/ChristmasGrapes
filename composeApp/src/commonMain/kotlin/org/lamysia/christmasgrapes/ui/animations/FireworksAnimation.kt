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
fun FireworksAnimation() {
    val fireworks = remember { mutableStateListOf<Firework>() }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (fireworks.size < 3) {
                fireworks.add(
                    Firework(
                        x = Random.nextFloat() * 0.8f + 0.1f,
                        y = 1f,
                        targetY = Random.nextFloat() * 0.5f + 0.2f,
                        color = listOf(
                            Color(0xFFFF0000),
                            Color(0xFF00FF00),
                            Color(0xFF0000FF),
                            Color(0xFFFFFF00),
                            Color(0xFFFF00FF)
                        ).random()
                    )
                )
            }
            fireworks.forEach { it.update() }
            fireworks.removeAll { it.particles.all { particle -> particle.alpha <= 0f } }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        fireworks.forEach { firework ->
            if (!firework.hasExploded) {
                drawCircle(
                    color = firework.color,
                    radius = 5.dp.toPx(),
                    center = Offset(
                        x = firework.x * size.width,
                        y = firework.y * size.height
                    )
                )
            } else {
                firework.particles.forEach { particle ->
                    drawCircle(
                        color = firework.color,
                        radius = 3.dp.toPx() * particle.scale,
                        center = Offset(
                            x = (firework.x + particle.x) * size.width,
                            y = (firework.y + particle.y) * size.height
                        ),
                        alpha = particle.alpha
                    )
                }
            }
        }
    }
}

data class Firework(
    val x: Float,
    var y: Float,
    val targetY: Float,
    val color: Color,
) {
    var hasExploded = false
    var particles = mutableListOf<FireworkParticle>()

    fun update() {
        if (!hasExploded) {
            y -= 0.02f
            if (y <= targetY) {
                hasExploded = true
                // Patlama parçacıklarını oluştur
                repeat(30) {
                    val angle = Random.nextFloat() * 2 * PI
                    val velocity = Random.nextFloat() * 0.02f + 0.01f
                    particles.add(
                        FireworkParticle(
                            x = 0f,
                            y = 0f,
                            velocityX = cos(angle).toFloat() * velocity,
                            velocityY = sin(angle).toFloat() * velocity,
                            scale = Random.nextFloat() * 0.5f + 0.5f
                        )
                    )
                }
            }
        } else {
            particles.forEach { it.update() }
        }
    }
}

data class FireworkParticle(
    var x: Float,
    var y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val scale: Float,
    var alpha: Float = 1f
) {
    fun update() {
        x += velocityX
        y += velocityY
        alpha -= 0.02f
    }
}