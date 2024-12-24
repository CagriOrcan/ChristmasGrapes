package org.lamysia.christmasgrapes.ui.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val requiresPremium: Boolean = false
) {
    data object Home : NavItem("home", Icons.Default.Home, "Home")
    data object MakeWish : NavItem("make_wish", Icons.Default.Favorite, "Make a Wish", true)
    data object Wishes : NavItem("wishes", Icons.Default.List, "Wishes", true)
}