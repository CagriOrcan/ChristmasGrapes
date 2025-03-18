package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.grapes
import christmasgrapes.composeapp.generated.resources.snowy
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.theme.AppTheme
import org.lamysia.christmasgrapes.ui.viewmodel.MakeWishViewModel

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    wishes: List<Wish>,
    isPremium: Boolean = false,
    viewModel: MakeWishViewModel,
    onGenerateWish: (String, Boolean) -> Unit = { _, _ -> }
) {
    var isShaking by remember { mutableStateOf(false) }
    var showMonthDialog by remember { mutableStateOf(false) }
    var selectedMonthIndex by remember { mutableStateOf(0) }
    var generatedWishText by remember { mutableStateOf<String?>(null) }
    var showTapHint by remember { mutableStateOf(true) }

    val shakeController = rememberInfiniteTransition()
    val shake by shakeController.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        )
    )

    val pulseAnim = rememberInfiniteTransition()
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val hintAlpha by animateFloatAsState(
        targetValue = if (showTapHint) 1f else 0f,
        animationSpec = tween(500)
    )

    LaunchedEffect(isShaking) {
        if (isShaking) {
            delay(300)
            try {
                val generatedWish = viewModel.generateWish()  // ViewModel üzerinden isteği atalım
                onGenerateWish(generatedWish, isPremium)
            } finally {
                isShaking = false
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = "Snowy Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AnimatedVisibility(
            visible = showTapHint,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Primary.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tap the grapes to make a wish!",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Grapes
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(Res.drawable.grapes),
                contentDescription = "Tap to make a wish",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .scale(scale)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isShaking = true
                        showTapHint = false
                    }
                    .graphicsLayer {
                        if (isShaking) {
                            rotationZ = shake * 5f
                            translationX = shake * 5f
                        }
                    }
            )
        }
    }
}

@Composable
private fun MonthChip(
    month: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) AppColors.Primary
                else AppColors.Primary.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = month.take(3),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else AppColors.Primary
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val sampleWishes = listOf(
        Wish(id = 0, text = "First wish", isPremium = false),
        Wish(id = 1, text = "Second wish", isPremium = false),
        Wish(id = 2, text = "Premium wish", isPremium = true)
    )

    AppTheme {
        HomeScreen(
            wishes = sampleWishes,
            isPremium = false,
            onGenerateWish = { _, _ -> },
            viewModel = MakeWishViewModel()
        )
    }
}