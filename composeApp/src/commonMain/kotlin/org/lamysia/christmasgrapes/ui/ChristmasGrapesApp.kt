package org.lamysia.christmasgrapes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.WishDialog
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.PremiumScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen

@Composable
fun ChristmasGrapesApp() {
    var wishes by remember { mutableStateOf(
        List(12) { index ->
            Wish(
                id = index,
                text = "",
                isLocked = index >= 3,
                hasWish = false
            )
        }
    ) }
    var isPremium by remember { mutableStateOf(false) }
    var selectedWish by remember { mutableStateOf<Wish?>(null) }
    var currentScreen by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            // Bottom Navigation implementation here
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                0 -> HomeScreen(
                    wishes = wishes,
                    isPremium = isPremium,
                    onWishClick = { wish -> selectedWish = wish },
                    onUpgradeToPremium = { isPremium = true }
                )
                1 -> if (isPremium) {
                    WishesScreen(
                        wishes = wishes.filter { it.hasWish },
                        onDeleteWish = { id ->
                            wishes = wishes.map { wish ->
                                if (wish.id == id) wish.copy(text = "", hasWish = false)
                                else wish
                            }
                        }
                    )
                } else {
                    PremiumScreen(
                        onUpgradeClick = { isPremium = true }
                    )
                }
            }
        }
    }

    selectedWish?.let { wish ->
        WishDialog(
            wish = wish,
            onDismiss = { selectedWish = null },
            onSave = { text ->
                wishes = wishes.map {
                    if (it.id == wish.id) it.copy(text = text) else it
                }
                selectedWish = null
            }
        )
    }
}