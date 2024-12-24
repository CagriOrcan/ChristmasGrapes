package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors

@Composable
fun WishesScreen(
    wishes: List<Wish> = emptyList(),
    onDeleteWish: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "My Wishes",
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.Primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (wishes.isEmpty()) {
            EmptyWishesContent()
        } else {
            WishesList(
                wishes = wishes,
                onDeleteWish = onDeleteWish
            )
        }
    }
}

@Composable
private fun EmptyWishesContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppColors.Primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No wishes yet",
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.Primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Make your first wish by clicking the button below",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.Secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun WishesList(
    wishes: List<Wish>,
    onDeleteWish: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = wishes,
            key = { it.id }
        ) { wish ->
            WishItem(
                wish = wish,
                onDelete = { onDeleteWish(wish.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishItem(
    wish: Wish,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wish.text,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = wish.dateCreated,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.Secondary
                )
            }

            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppColors.Error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete wish"
                )
            }
        }
    }
}
