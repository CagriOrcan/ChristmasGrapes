package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish

@Preview
@Composable
private fun GrapesGrid(
    wishes: List<Wish>, // Update parameter type
    isPremium: Boolean,
    onWishClick: (Wish) -> Unit = {}, // Add click handler
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        items(wishes) { wish ->
            GrapeItem(
                wish = wish,
                onClick = { onWishClick(wish) }
            )
        }
    }
}
