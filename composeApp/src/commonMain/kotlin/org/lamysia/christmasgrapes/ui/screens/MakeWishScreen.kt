package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.snowy
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.ui.components.PremiumBanner
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.theme.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MakeWishScreen(
    isPremium: Boolean = false,
    wishCount: Int = 0,
    onWishMade: (String) -> Unit = {},
    onUpgradeToPremium: () -> Unit = {}
) {
    var wishText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Kar taneli arka plan
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
            if (!isPremium) {
                PremiumBanner(
                    onUpgradeClick = onUpgradeToPremium
                )
            }

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

                    OutlinedTextField(
                        value = wishText,
                        onValueChange = {
                            if (it.length <= 100) { // Karakter sınırı
                                wishText = it
                            }
                        },
                        label = { Text("Enter your wish") },
                        placeholder = { Text("What's your wish for the new year?") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.Primary,
                            focusedLabelColor = AppColors.Primary
                        ),
                        maxLines = 3,
                        singleLine = false
                    )

                    Text(
                        text = "${wishText.length}/100",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (wishText.length >= 100) AppColors.Error else AppColors.Secondary,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (wishText.isNotBlank()) {
                                onWishMade(wishText)
                                wishText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        ),
                        enabled = wishText.isNotBlank() && (isPremium || wishCount >= 3)
                    ) {
                        Text("Save Wish")
                    }

                    if (!isPremium) {
                        Text(
                            text = "Free wishes remaining: ${3 - wishCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.Secondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MakeWishScreenPreview() {
    AppTheme {
        MakeWishScreen()
    }
}