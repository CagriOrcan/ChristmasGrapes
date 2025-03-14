package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
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

       /* if (!loading && wishes.isEmpty()) {
            EmptyWishesState(
                modifier = Modifier.align(Alignment.Center)
            )
        }*/

        if (showSummary) {
            SummaryScreen(
                wishes = wishes,
                onBackClick = { showSummary = false }
            )
        } else if (selectedMonth != null) {
            println("WishesScreen: Showing MonthDetailScreen for month: $selectedMonth")
            MonthDetailScreen(
                monthName = selectedMonth!!,
                wishes = wishes.filter { it.assignedMonth == monthNames.indexOf(selectedMonth) + 1 }.also {
                    println("WishesScreen: Filtered wishes for month: ${it.map { wish -> "${wish.text}(completed=${wish.isCompleted})" }}")
                },
                onBackClick = { selectedMonth = null },
                onWishComplete = { updatedWish ->
                    println("WishesScreen: onWishComplete called for wish: ${updatedWish.text}, new isCompleted: ${updatedWish.isCompleted}")
                    viewModel.toggleWishCompletion(updatedWish.id!!, updatedWish.isCompleted)
                },
                onDeleteWish = { wish ->
                    viewModel.deleteWish(wish.id!!)
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Summary Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { showSummary = true },
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.Primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = AppColors.Primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Annual Summary",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }

                // Months Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(monthNames) { monthName ->
                        MonthCard(
                            monthName = monthName,
                            wishes = wishes.filter { it.assignedMonth == monthNames.indexOf(monthName) + 1 },
                            onMonthClick = { selectedMonth = monthName },
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
    var localDragOffset by remember { mutableStateOf(Offset.Zero) }
    val monthIndex = monthNames.indexOf(monthName)

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
                .padding(8.dp)
        ) {
            Text(
                text = monthName,
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                Text(
                    text = if(!wishes.isNullOrEmpty()) "\n${wishes.size}" else "",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = AppColors.Secondary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun EmptyWishesState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = AppColors.Secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Wishes Yet",
            style = MaterialTheme.typography.titleLarge,
            color = AppColors.Secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your wishes will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.Secondary.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun WishItem(
    wish: List<Wish>,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "${wish.size}",
            style = MaterialTheme.typography.titleLarge,
            color = AppColors.Primary
        )
    }
}