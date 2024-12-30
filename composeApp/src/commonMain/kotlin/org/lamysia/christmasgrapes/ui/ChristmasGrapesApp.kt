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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.WishDialog
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.PremiumScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen

@Preview
@Composable
fun ChristmasGrapesApp() {
    var wishes by remember { mutableStateOf(
        List(12) { index ->
            Wish(
                id = index,
                text = "",
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
                    isPremium = isPremium,
                    wishes = wishes,
                    onGenerateWish = { text, isPrem ->
                        val newWish = Wish(
                            id = wishes.size,
                            text = text,
                            isPremium = isPrem,
                            hasWish = true
                        )
                        selectedWish = newWish
                    }
                )
                1 -> if (isPremium) {
                    WishesScreen() // ViewModel içinden wishes'i alacak
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
            onSave = { savedWish ->
                wishes = wishes.map {
                    if (it.id == savedWish.id) savedWish else it
                }
                selectedWish = null
            }
        )
    }
}