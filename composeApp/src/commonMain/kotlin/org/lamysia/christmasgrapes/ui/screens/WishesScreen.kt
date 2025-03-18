package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
import christmasgrapes.composeapp.generated.resources.wishes
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.viewmodel.MakeWishViewModel

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private fun String.toFormattedDate(): String {
    return try {
        val instant = Instant.parse(this)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${dateTime.dayOfMonth} ${monthNames[dateTime.monthNumber - 1]} ${dateTime.year}"
    } catch (e: Exception) {
        this
    }
}

@Composable
private fun AnnualSummaryButton(
    wishes: List<Wish>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalWishes = wishes.size
    val completedWishes = wishes.count { it.isCompleted }
    val completionPercentage = if (totalWishes > 0) 
        (completedWishes.toFloat() / totalWishes * 100).toInt() else 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .height(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Primary.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Annual Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "${wishes.size} wishes in total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$completionPercentage%",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WishesScreen(
    modifier: Modifier = Modifier,
    viewModel: MakeWishViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadWishes()
    }

    val wishes by viewModel.wishes.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var draggedWish by remember { mutableStateOf<Wish?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var targetMonthIndex by remember { mutableStateOf<Int?>(null) }
    var showSummary by remember { mutableStateOf(false) }
    
    // Animation states
    var selectedMonthCardBounds by remember { mutableStateOf<IntRect?>(null) }
    val animationProgress by animateFloatAsState(
        targetValue = if (selectedMonth != null) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Snowy background
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AnimatedVisibility(
            visible = loading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = AppColors.Primary,
                strokeWidth = 4.dp
            )
        }

        if (!loading && wishes.isEmpty()) {
            EmptyWishesState(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (showSummary) {
            SummaryScreen(
                wishes = wishes,
                onBackClick = { showSummary = false }
            )
        } else if (selectedMonth != null && animationProgress == 1f) {
            MonthDetailScreen(
                monthName = selectedMonth!!,
                wishes = wishes.filter { it.assignedMonth == monthNames.indexOf(selectedMonth) + 1 },
                onBackClick = { 
                    selectedMonth = null
                    selectedMonthCardBounds = null
                },
                onWishComplete = { updatedWish ->
                    viewModel.toggleWishCompletion(updatedWish.id!!, updatedWish.isCompleted)
                },
                onDeleteWish = { wish ->
                    viewModel.deleteWish(wish.id!!)
                },
                modifier = Modifier.graphicsLayer {
                    alpha = animationProgress
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Replace the existing Summary Button with new one
                AnnualSummaryButton(
                    wishes = wishes,
                    onClick = { showSummary = true }
                )

                val monthsWithWishes = monthNames
                    .mapIndexed { index, monthName ->
                        monthName to wishes.filter { it.assignedMonth == index + 1 }
                    }
                    .filter { (_, monthWishes) -> monthWishes.isNotEmpty() }
                    .sortedBy { (monthName, _) -> monthNames.indexOf(monthName) }

                // Months Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = 1f - animationProgress
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(monthsWithWishes.size) { index ->
                        val (monthName, monthWishes) = monthsWithWishes[index]

                        MonthCard(
                            monthName = monthName,
                            wishes = wishes.filter { it.assignedMonth == monthNames.indexOf(monthName) + 1 },
                            onMonthClick = { 
                                selectedMonth = monthName
                            },
                            onDragStart = { wish ->
                                draggedWish = wish
                                isDragging = true
                                targetMonthIndex = null
                            },
                            onDragEnd = { month ->
                                if (draggedWish != null && targetMonthIndex != null) {
                                    viewModel.updateWishMonth(draggedWish!!.id!!, targetMonthIndex!! + 1)
                                }
                                draggedWish = null
                                isDragging = false
                                dragOffset = Offset.Zero
                                targetMonthIndex = null
                            },
                            onDragCancel = {
                                draggedWish = null
                                isDragging = false
                                dragOffset = Offset.Zero
                                targetMonthIndex = null
                            },
                            onDelete = { wish -> viewModel.deleteWish(wish.id!!) },
                            onDragUpdate = { newOffset, monthIndex -> 
                                dragOffset = newOffset
                                targetMonthIndex = monthIndex
                            },
                            isTargeted = monthNames.indexOf(monthName) == targetMonthIndex,
                            isDragging = isDragging
                        )
                    }
                }

                // Dragged wish preview
                if (isDragging && draggedWish != null) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(dragOffset.x.toInt(), dragOffset.y.toInt()) }
                            .zIndex(1f)
                            .alpha(0.9f)
                    ) {
                        WishItem(
                            wish = listOf(draggedWish!!),
                            onDelete = {},
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = error != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            error?.let { errorMessage ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.Error.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = AppColors.Error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthCard(
    monthName: String,
    wishes: List<Wish>,
    onMonthClick: () -> Unit,
    onDragStart: (Wish) -> Unit,
    onDragEnd: (Int) -> Unit,
    onDragCancel: () -> Unit,
    onDelete: (Wish) -> Unit,
    onDragUpdate: (Offset, Int) -> Unit,
    isTargeted: Boolean,
    isDragging: Boolean,
    modifier: Modifier = Modifier
) {
    if(wishes.isNullOrEmpty()) return

    var localDragOffset by remember { mutableStateOf(Offset.Zero) }
    val monthIndex = monthNames.indexOf(monthName)
    
    // Calculate statistics
    val completedWishes = wishes.count { it.isCompleted }
    val completionPercentage = (completedWishes.toFloat() / wishes.size * 100).toInt()
    val isHighProgress = completionPercentage >= 75
    val isMediumProgress = completionPercentage in 30..74

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isTargeted && isDragging) 
                    AppColors.Primary.copy(alpha = 0.1f) 
                else AppColors.Background.copy(alpha = 0.9f)
            )
            .border(
                width = if (isTargeted && isDragging) 2.dp else 1.dp,
                color = if (isTargeted && isDragging) AppColors.Primary else AppColors.Primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onMonthClick),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Month name with small indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = when {
                                isHighProgress -> Color(0xFF4CAF50)
                                isMediumProgress -> Color(0xFFFFA726)
                                else -> AppColors.Primary
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.Primary
                )
            }

            // Wishes count and progress
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = wishes.size.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppColors.Primary
                )
                Text(
                    text = if(wishes.size == 1) "Wish" else "Wishes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Primary.copy(alpha = 0.7f)
                )
            }

            // Progress indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(completionPercentage / 100f)
                        .height(4.dp)
                        .background(
                            when {
                                isHighProgress -> Color(0xFF4CAF50)
                                isMediumProgress -> Color(0xFFFFA726)
                                else -> AppColors.Primary
                            },
                            RoundedCornerShape(2.dp)
                        )
                )
            }

            // Completion status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$completedWishes/${wishes.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.Primary.copy(alpha = 0.7f)
                )
                Text(
                    text = "$completionPercentage%",
                    style = MaterialTheme.typography.bodySmall,
                    color = when {
                        isHighProgress -> Color(0xFF4CAF50)
                        isMediumProgress -> Color(0xFFFFA726)
                        else -> AppColors.Primary
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyWishesState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(AppColors.Background.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = AppColors.Primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.wishes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                tint = AppColors.Primary.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Make Your First Wish",
                style = MaterialTheme.typography.headlineSmall,
                color = AppColors.Primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Start by adding wishes to any month\nand watch your dreams come true",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.Primary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Hint card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Primary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.wishes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = AppColors.Primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Tap + to add a new wish",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.Primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WishItem(
    wish: List<Wish>,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = wish.firstOrNull()?.text ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppColors.Primary
                    )
                    if (wish.size > 1) {
                        Text(
                            text = "+${wish.size - 1} more wishes",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.Primary.copy(alpha = 0.7f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = AppColors.Primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = wish.size.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.Primary
                    )
                }
            }
        }
    }
}