package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllOut
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDetailScreen(
    monthName: String,
    wishes: List<Wish>,
    onBackClick: () -> Unit,
    onWishComplete: (Wish) -> Unit,
    onDeleteWish: (Wish) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf<Wish?>(null) }
    var progressValue by remember { mutableStateOf(0f) }
    var isLoaded by remember { mutableStateOf(false) }
    var scrollToTopTrigger by remember { mutableStateOf(false) }
    
    // Calculate completion percentage for progress bar
    val completedWishes = wishes.count { it.isCompleted }
    val completionPercentage = if (wishes.isNotEmpty()) {
        completedWishes.toFloat() / wishes.size
    } else 0f
    
    // Animation states
    val headerTranslation by animateFloatAsState(
        targetValue = if (isLoaded) 0f else -100f,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
    )
    
    val cardScale by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0.8f,
        animationSpec = tween(700, easing = FastOutSlowInEasing)
    )
    
    // Progress animation
    LaunchedEffect(completionPercentage) {
        progressValue = 0f
        delay(300)
        progressValue = completionPercentage
    }
    
    // Load animation
    LaunchedEffect(Unit) {
        isLoaded = false
        delay(100)
        isLoaded = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Background with blur effect
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(if (showConfirmDialog != null) 3.dp else 0.dp),
            contentScale = ContentScale.Crop
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with animated slide-in
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .graphicsLayer { 
                        translationY = headerTranslation 
                    },
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Primary
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = monthName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .clickable { scrollToTopTrigger = !scrollToTopTrigger },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Scroll to top",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Card with scale animation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { 
                        scaleX = cardScale
                        scaleY = cardScale
                        alpha = cardScale
                    },
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Background.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Month title with emoji
                    Text(
                        text = "✨ $monthName Wishes ✨",
                        style = MaterialTheme.typography.titleLarge,
                        color = AppColors.Primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Progress indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Gray.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressValue)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        progressValue >= 0.75f -> Color(0xFF4CAF50)
                                        progressValue >= 0.3f -> AppColors.PendingColor
                                        else -> AppColors.Primary
                                    }
                                )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Statistics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Total wishes stat
                        StatisticItem(
                            icon = Icons.Default.AllOut,
                            count = wishes.size,
                            label = "Total",
                            color = AppColors.Primary
                        )
                        
                        // Completed wishes stat
                        StatisticItem(
                            icon = Icons.Default.CheckCircle,
                            count = completedWishes,
                            label = "Completed",
                            color = Color(0xFF4CAF50)
                        )
                        
                        // Pending wishes stat
                        StatisticItem(
                            icon = Icons.Default.Pending,
                            count = wishes.size - completedWishes,
                            label = "Pending",
                            color = AppColors.PendingColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Section labels with different colors for completed and pending
            if (wishes.any { it.isCompleted }) {
               /* SectionLabel(
                    title = "Completed Wishes",
                    count = completedWishes,
                    color = Color(0xFF4CAF50)
                )*/
            }
            
            if (wishes.any { !it.isCompleted }) {
                if (wishes.any { it.isCompleted }) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                /*SectionLabel(
                    title = "Pending Wishes",
                    count = wishes.count { !it.isCompleted },
                    color = AppColors.PendingColor
                )*/
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Wishes List with staggered animation
            val rememberScrollState = rememberScrollState()
            
            // Reset scroll position when trigger changes
            LaunchedEffect(scrollToTopTrigger) {
                rememberScrollState.scrollTo(0)
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                // First show completed wishes
                val completedWishesList = wishes.filter { it.isCompleted }
                if (completedWishesList.isNotEmpty()) {
                    itemsIndexed(completedWishesList) { index, wish ->
                        MonthDetailWishItem(
                            wish = wish,
                            onComplete = { onWishComplete(wish.copy(isCompleted = !wish.isCompleted)) },
                            onDelete = { showConfirmDialog = wish },
                            animationDelay = index * 50L
                        )
                    }
                }
                
                // Then show pending wishes
                val pendingWishesList = wishes.filter { !it.isCompleted }
                if (pendingWishesList.isNotEmpty()) {
                    itemsIndexed(pendingWishesList) { index, wish ->
                        MonthDetailWishItem(
                            wish = wish,
                            onComplete = { onWishComplete(wish.copy(isCompleted = !wish.isCompleted)) },
                            onDelete = { showConfirmDialog = wish },
                            animationDelay = (completedWishesList.size + index) * 50L
                        )
                    }
                }
            }
        }
        
        // Empty state
        if (wishes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(32.dp)
                        .graphicsLayer { 
                            scaleX = cardScale
                            scaleY = cardScale
                            alpha = cardScale
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.Background.copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = AppColors.Primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Wishes for $monthName Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.Primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Make a wish to start your collection!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.Primary.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Confirmation dialog
        if (showConfirmDialog != null) {
            Dialog(onDismissRequest = { showConfirmDialog = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.Background
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AppColors.Error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Delete Wish?",
                            style = MaterialTheme.typography.titleLarge,
                            color = AppColors.Primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Are you sure you want to delete this wish? This action cannot be undone.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = AppColors.Primary.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { showConfirmDialog = null },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Button(
                                onClick = {
                                    showConfirmDialog?.let {
                                        onDeleteWish(it)
                                        showConfirmDialog = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.Error
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun SectionLabel(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background.copy(alpha = 0.9f)
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = color,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MonthDetailWishItem(
    wish: Wish,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    animationDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val completedColor = Color(0xFF4CAF50) // Material Green
    val pendingColor = AppColors.PendingColor   // Material Orange

    var animatedProgress by remember { mutableStateOf(0f) }
    
    // Staggered animation on first appearance
    LaunchedEffect(Unit) {
        delay(animationDelay)
        animatedProgress = 1f
    }

    val slideAnimation by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = slideAnimation
                translationX = (1f - slideAnimation) * 100f
            }
            .border(
                width = 1.dp,
                color = if (wish.isCompleted) completedColor else pendingColor,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Wish icon based on completion state
                // Checkbox with ripple effect
                IconButton(
                    onClick = onComplete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Checkbox(
                        checked = wish.isCompleted,
                        onCheckedChange = { onComplete() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = completedColor,
                            uncheckedColor = pendingColor.copy(alpha = 0.6f)
                        )
                    )
                }
            // Action buttons row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (wish.isCompleted) completedColor.copy(alpha = 0.1f)
                            else pendingColor.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (wish.isCompleted) Icons.Default.CheckCircle else Icons.Default.Pending,
                        contentDescription = null,
                        tint = if (wish.isCompleted) completedColor else pendingColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = wish.text,
                        style = MaterialTheme.typography.titleSmall,
                        textDecoration = if (wish.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (wish.isCompleted) completedColor else Color.Unspecified,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            }

                
                // Delete button with error color
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete wish",
                        tint = AppColors.Error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

@Composable
fun MutableInteractionSource.collectIsHoveredAsState(): State<Boolean> {
    val isHovered = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        // In case of web target this would actually track hover state
        // For now we'll just return false
    }
    return isHovered
} 