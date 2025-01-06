package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MonthDetailScreen(
    monthName: String,
    wishes: List<Wish>,
    onBackClick: () -> Unit,
    onWishComplete: (Wish) -> Unit,
    onDeleteWish: (Wish) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppColors.Primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = monthName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                // Placeholder for symmetry
                Spacer(modifier = Modifier.size(48.dp))
            }

            // Summary Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Background.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = wishes.size.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = AppColors.Primary
                        )
                        Text(
                            text = "Total Wishes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.Primary
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = wishes.count { it.isCompleted }.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            // Wishes List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(wishes) { wish ->
                    var isCompleted by remember { mutableStateOf(wish.isCompleted) }

                    MonthDetailWishItem(
                        wish = wish.copy(isCompleted = isCompleted),
                        onComplete = {
                            isCompleted = !isCompleted
                            onWishComplete(wish.copy(isCompleted = isCompleted))
                        },
                        onDelete = { onDeleteWish(wish) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthDetailWishItem(
    wish: Wish,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedColor = Color(0xFF4CAF50) // Material Green
    println("Rendering wish: ${wish.text}, isCompleted: ${wish.isCompleted}")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (wish.isCompleted) completedColor else AppColors.Primary,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = wish.isCompleted,
                    onCheckedChange = { newCheckedState ->
                        println("Checkbox clicked for wish: ${wish.text}, current state: ${wish.isCompleted}, new state: $newCheckedState")
                        onComplete()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = completedColor,
                        uncheckedColor = AppColors.Primary.copy(alpha = 0.6f)
                    )
                )
                
                Text(
                    text = wish.text,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (wish.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (wish.isCompleted) completedColor else Color.Unspecified,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
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