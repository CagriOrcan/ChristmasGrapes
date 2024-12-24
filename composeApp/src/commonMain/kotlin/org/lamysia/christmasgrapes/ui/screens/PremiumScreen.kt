package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.lamysia.christmasgrapes.ui.theme.AppColors

@Composable
fun PremiumScreen(
    onUpgradeClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PremiumHeader()
        }

        item {
            PremiumFeatures()
        }

        item {
            PremiumPricing(onUpgradeClick)
        }
    }
}

@Composable
private fun PremiumHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = AppColors.Primary,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Unlock Premium Features",
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.Primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Get access to all features and make unlimited wishes",
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.Secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PremiumFeatures() {
    val features = listOf(
        "Make unlimited wishes" to "Save as many wishes as you want",
        "Access all grapes" to "Unlock all 12 grapes for maximum luck",
        "Premium support" to "Get priority support for your questions",
        "Ad-free experience" to "Enjoy the app without interruptions",
        "Future updates" to "Get access to all upcoming features"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        features.forEach { (title, description) ->
            FeatureItem(title, description)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = AppColors.Secondary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Secondary
            )
        }
    }
}

@Composable
private fun PremiumPricing(onUpgradeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.SurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$9",
                style = MaterialTheme.typography.displayLarge,
                color = AppColors.Primary
            )

            Text(
                text = "One-time payment",
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.Secondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onUpgradeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                Text(
                    text = "Upgrade Now",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Secure payment via App Store",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.Secondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}