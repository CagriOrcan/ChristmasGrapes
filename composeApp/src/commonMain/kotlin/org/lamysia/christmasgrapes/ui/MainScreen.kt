package org.lamysia.christmasgrapes.ui

/*import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions*/
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowflake
import christmasgrapes.composeapp.generated.resources.wishes
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.SuccessDialog
import org.lamysia.christmasgrapes.ui.components.WishDialog
import org.lamysia.christmasgrapes.ui.screens.HomeScreen
import org.lamysia.christmasgrapes.ui.screens.MakeWishScreen
import org.lamysia.christmasgrapes.ui.screens.WishesScreen
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.viewmodel.MakeWishViewModel

/*import org.lamysia.christmasgrapes.ui.viewmodel.PremiumViewModel*/

@Composable
fun MainScreen(
    viewModel: MakeWishViewModel = remember { MakeWishViewModel() },
    isPremium: Boolean = true
) {
    //val premiumViewModel = remember { PremiumViewModel() }
    var selectedItem by remember { mutableStateOf(0) }
    var showWishCard by remember { mutableStateOf(false) }
    var generatedWish by remember { mutableStateOf("") }
    var showPremiumScreen by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val wishes by viewModel.wishes.collectAsState()

   /* val options = remember {
        PaywallOptions(dismissRequest = { showPremiumScreen = false }) {
            shouldDisplayDismissButton = true
        }
    }*/

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                if (showSuccessDialog) {
                    SuccessDialog(
                        onDismiss = {
                            showSuccessDialog = false
                        }
                    )
                }

                NavigationBar(
                    containerColor = AppColors.Primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),

                ) {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp),
                                tint = if (selectedItem == 0) Color.White else Color.White,
                            )
                        },
                        label = { Text("Home") },
                        selected = selectedItem == 0,
                        onClick = {
                            selectedItem = 0
                            showPremiumScreen = false
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White,
                            selectedTextColor = AppColors.Background,
                            unselectedTextColor = AppColors.Surface,
                            indicatorColor = AppColors.PrimaryDark
                        ),
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(Res.drawable.wishes),
                                contentDescription = "Wishes",
                                modifier = Modifier.size(24.dp),
                                tint = if (selectedItem == 2) Color.White else Color.White,
                            )
                        },
                        label = { Text("Wishes") },
                        selected = selectedItem == 2,
                        onClick = {
                            if (isPremium) {
                                selectedItem = 2
                                showPremiumScreen = false
                            } else {
                                showPremiumScreen = true
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
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            // Docked FAB
            FloatingActionButton(
                onClick = {
                    if (isPremium) {
                        selectedItem = 1
                        showPremiumScreen = false
                    } else {
                        showPremiumScreen = true
                    }
                },
                containerColor = AppColors.Background,
                contentColor = AppColors.Primary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(72.dp)
                    .offset(y = 56.dp)
                    .border(2.dp, AppColors.Primary, CircleShape),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.snowflake),
                    contentDescription = "Make a Wish",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (showPremiumScreen) {
              //  Paywall(options = options)
            } else {
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
                            onWishMade = { wish ->
                                viewModel.addWish(
                                    text = wish.text,
                                    isPremium = wish.isPremium,
                                    assignedMonth = wish.assignedMonth
                                )

                            }
                        )
                    }

                    2 -> if (isPremium) {
                        WishesScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    if (showWishCard) {
        WishDialog(
            wish = Wish(
                id = null,
                text = generatedWish,
                isPremium = isPremium,
                hasWish = true
            ),
            isLoading = false,
            error = null,
            onDismiss = {
                showWishCard = false
            },
            onSave = { wish ->
                viewModel.addWish(wish.text, isPremium)
                showWishCard = false
                showSuccessDialog = true
            },
            showShareButton = true
        )
    }
}
