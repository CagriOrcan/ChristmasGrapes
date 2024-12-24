package org.lamysia.christmasgrapes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.MakeWishBottomSheet
import org.lamysia.christmasgrapes.ui.navigations.NavItem
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.PremiumScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen
import org.lamysia.christmasgrapes.ui.theme.AppColors

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isPremium: Boolean = false,
) {
    var selectedItem by remember { mutableStateOf(0) }
    var showMakeWishSheet by remember { mutableStateOf(false) }
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
                containerColor = AppColors.PrimaryDark,
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
                                    // Show premium dialog
                                }
                                item == NavItem.MakeWish -> {
                                    showMakeWishSheet = true
                                }
                                else -> selectedItem = index
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
        },
        floatingActionButton = {
            if (selectedItem == 1) {
                FloatingActionButton(
                    onClick = {
                        if (isPremium) {
                            showMakeWishSheet = true
                        } else {
                            // Show premium dialog
                        }
                    },
                    containerColor = AppColors.Primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Make a Wish",
                        tint = AppColors.Background
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> HomeScreen(
                    wishes = wishes,
                    isPremium = isPremium,
                    onWishClick = { wish ->
                        if (!wish.isLocked) {
                            showMakeWishSheet = true
                        }
                    }
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

    if (showMakeWishSheet) {
        MakeWishBottomSheet(
            onDismiss = { showMakeWishSheet = false },
            onSave = { wishText: String -> // Explicit parameter type
                val nextAvailableId = wishes.indexOfFirst { !it.hasWish }
                if (nextAvailableId != -1) {
                    wishes = wishes.mapIndexed { index, wish ->
                        if (index == nextAvailableId) {
                            wish.copy(text = wishText, hasWish = true)
                        } else wish
                    }
                }
                showMakeWishSheet = false
            }
        )
    }
}