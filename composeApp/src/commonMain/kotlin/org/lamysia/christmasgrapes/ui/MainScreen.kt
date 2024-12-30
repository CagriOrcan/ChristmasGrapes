package org.lamysia.christmasgrapes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.WishDialog
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.MakeWishScreen
import org.lamysia.christmasgrapes.ui.screens.PremiumScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.viewmodel.MakeWishViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MakeWishViewModel = viewModel(),
    isPremium: Boolean = true
) {
    var selectedItem by remember { mutableStateOf(0) }
    var showWishCard by remember { mutableStateOf(false) }
    var generatedWish by remember { mutableStateOf("") }
    val wishes by viewModel.wishes.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = AppColors.Primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = if (selectedItem == 0) AppColors.Background else AppColors.Surface
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppColors.Background,
                        unselectedIconColor = AppColors.Surface,
                        selectedTextColor = AppColors.Background,
                        unselectedTextColor = AppColors.Surface,
                        indicatorColor = AppColors.PrimaryDark
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Make a Wish",
                            tint = if (selectedItem == 1) AppColors.Background else AppColors.Surface
                        )
                    },
                    label = { Text("Make a Wish") },
                    selected = selectedItem == 1,
                    onClick = {
                        if (isPremium) {
                            selectedItem = 1
                        } else {
                            // Premium dialog göster
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

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Wishes",
                            tint = if (selectedItem == 2) AppColors.Background else AppColors.Surface
                        )
                    },
                    label = { Text("Wishes") },
                    selected = selectedItem == 2,
                    onClick = {
                        if (isPremium) {
                            selectedItem = 2
                        } else {
                            // Premium dialog göster
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
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> HomeScreen(
                    wishes = wishes,
                    isPremium = isPremium,
                    onGenerateWish = { wishText, isPrem ->
                        generatedWish = wishText
                        showWishCard = true
                    },
                    viewModel = viewModel
                )
                1 -> if (isPremium) {
                    MakeWishScreen(
                        isPremium = isPremium,
                        onWishMade = { text ->
                            viewModel.addWish(text, isPremium)
                        }
                    )
                } else {
                    PremiumScreen()
                }
                2 -> if (isPremium) {
                    WishesScreen(viewModel = viewModel)
                } else {
                    PremiumScreen()
                }
            }
        }
    }

    if (showWishCard) {
        WishDialog(
            wish = Wish(
                id = null,
                text = generatedWish,  // HomeScreen'den gelen wish'i kullan
                isPremium = isPremium,
                hasWish = true
            ),
            isLoading = false,  // İstek zaten tamamlandı
            error = null,
            onDismiss = {
                showWishCard = false
            },
            onSave = { wish ->
                viewModel.addWish(wish.text, isPremium)
                showWishCard = false
            }
        )
    }
}
