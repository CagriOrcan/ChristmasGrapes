package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.components.WishDialog
import org.lamysia.christmasgrapes.ui.theme.AppColors

private val monthNames = listOf(
    "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
    "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeWishScreen(
    isPremium: Boolean = false,
    onWishMade: (Wish) -> Unit = {}
) {
    var wishText by remember { mutableStateOf("") }
    var showWishCard by remember { mutableStateOf(false) }
    var selectedMonthIndex by remember { mutableStateOf(0) }
    var isMonthDropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.snowy),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Background.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Make Your Wish",
                        style = MaterialTheme.typography.headlineMedium,
                        color = AppColors.Primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ay seçimi
                    ExposedDropdownMenuBox(
                        expanded = isMonthDropdownExpanded,
                        onExpandedChange = { isMonthDropdownExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = monthNames[selectedMonthIndex],
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select month"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppColors.Primary,
                                focusedLabelColor = AppColors.Primary
                            ),
                            label = { Text("Select Month") }
                        )

                        ExposedDropdownMenu(
                            expanded = isMonthDropdownExpanded,
                            onDismissRequest = { isMonthDropdownExpanded = false }
                        ) {
                            monthNames.forEachIndexed { index, month ->
                                DropdownMenuItem(
                                    text = { Text(month) },
                                    onClick = {
                                        selectedMonthIndex = index
                                        isMonthDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = wishText,
                        onValueChange = { wishText = it },
                        label = { Text("Enter your wish") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.Primary,
                            focusedLabelColor = AppColors.Primary
                        )
                    )

                    Button(
                        onClick = {
                            if (wishText.isNotBlank()) {
                                showWishCard = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        )
                    ) {
                        Text("Save Wish")
                    }
                }
            }
        }

        if (showWishCard) {
            val newWish = Wish(
                id = null,
                text = wishText,
                isPremium = isPremium,
                hasWish = true,
                assignedMonth = selectedMonthIndex + 1
            )
            WishDialog(
                wish = newWish,
                isLoading = false,
                error = null,
                onDismiss = { showWishCard = false },
                onSave = { wish ->
                    onWishMade(wish)
                    showWishCard = false
                },
                showShareButton = true
            )
        }
    }
}
