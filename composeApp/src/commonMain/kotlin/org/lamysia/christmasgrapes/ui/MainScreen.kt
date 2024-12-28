package org.lamysia.christmasgrapes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.data.OpenAIRepository
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.WishCard
import org.lamysia.christmasgrapes.ui.navigations.NavItem
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.MakeWishScreen
import org.lamysia.christmasgrapes.ui.screens.PremiumScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.viewmodel.MakeWishViewModel

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isPremium: Boolean = true,
) {
    val viewModel = remember { MakeWishViewModel() }
    var selectedItem by remember { mutableStateOf(0) }
    var showWishCard by remember { mutableStateOf(false) }
    var generatedWish by remember { mutableStateOf("") }
    val wishCount by viewModel.wishCount.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val openAIRepository = remember { OpenAIRepository() }

    var wishes by remember { mutableStateOf<List<Wish>>(
        List(12) { index ->
            Wish(
                id = index,
                text = "",
                isLocked = index >= 3,
                hasWish = false
            )
        }
    ) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = AppColors.Primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                val items = listOf(NavItem.Home, NavItem.MakeWish, NavItem.Wishes)
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (selectedItem == index) AppColors.Background else AppColors.Surface
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (selectedItem == index) AppColors.Background else AppColors.Surface
                            )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            when {
                                item.requiresPremium && !isPremium -> {
                                    // Premium dialog göster
                                }
                                else -> selectedItem = index // Direkt olarak ekranı değiştir
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppColors.Background,
                            unselectedIconColor = AppColors.Surface,
                            selectedTextColor = AppColors.Background,
                            unselectedTextColor = AppColors.Surface,
                            indicatorColor = AppColors.PrimaryDark
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> HomeScreen(
                    isPremium = isPremium,
                    onUpgradeToPremium = { /* premium logic */ },
                    onGenerateWish = {
                        coroutineScope.launch {
                           /* generatedWish = openAIRepository.generateWish()
                            showWishSheet = true*/
                            try {
                                generatedWish = openAIRepository.generateWish()
                                showWishCard = true
                            } catch (e: Exception) {
                                println("Wish generation error: ${e.message}")
                                e.printStackTrace()
                                // Hata durumunda varsayılan bir mesaj gösterelim
                                generatedWish = "May your wishes come true in the new year!"
                                showWishCard = true
                            }
                        }
                    },
                )
                1 -> MakeWishScreen(
                    isPremium = isPremium,
                    wishCount = wishCount,
                    onWishMade = { wish ->
                        if (!isPremium) {
                            viewModel.incrementWishCount()
                        }
                        generatedWish = wish
                        showWishCard = true
                    },
                    onUpgradeToPremium = { /* Premium logic */ }
                )
                2 -> if (isPremium) {
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
                    PremiumScreen()
                }
            }
        }
    }

    if (showWishCard) {
        WishCard(
            wish = generatedWish,
            onDismiss = { showWishCard = false }
        )
    }
}
