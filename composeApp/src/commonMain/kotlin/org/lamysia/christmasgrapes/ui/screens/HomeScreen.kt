package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.GrapeItem
import org.lamysia.christmasgrapes.ui.components.PremiumBanner
import org.lamysia.christmasgrapes.ui.theme.AppColors

@Preview
@Composable
fun HomeScreen(
    wishes: List<Wish>,
    isPremium: Boolean = false,
    onWishClick: (Wish) -> Unit = {},
    onUpgradeToPremium: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Christmas Grapes",
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.Primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Premium Banner - if not premium
        if (!isPremium) {
            PremiumBanner(
                onUpgradeClick = onUpgradeToPremium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Grapes Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(wishes) { wish ->
                GrapeItem(
                    wish = wish,
                    onClick = { onWishClick(wish) }
                )
            }
        }
    }
}