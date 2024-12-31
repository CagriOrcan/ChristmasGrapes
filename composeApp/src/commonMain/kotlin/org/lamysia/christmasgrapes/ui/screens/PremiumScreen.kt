package org.lamysia.christmasgrapes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.revenuecat.purchases.kmp.models.Package
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.viewmodel.PremiumViewModel

@Composable
fun PremiumScreen(
    onUpgradeSuccess: () -> Unit = {}
) {

    val viewModel = remember { PremiumViewModel() }
    val offerings by viewModel.offerings.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()

    LaunchedEffect(isPremium) {
        if (isPremium) {
            onUpgradeSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upgrade to Premium",
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.Primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Premium özellikleri
        PremiumFeatureItem(
            icon = Icons.Default.Star,
            title = "Unlimited Wishes",
            description = "Create and save as many wishes as you want"
        )

        Spacer(modifier = Modifier.height(24.dp))

        offerings?.getOffering("premium")?.let { offering ->
            offering.availablePackages.forEach { pkg ->
                PremiumPackageItem(
                    premiumPackage = pkg,
                    onSelect = {
                        if (!loading) {
                            viewModel.purchasePremium(pkg)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (loading) {
            CircularProgressIndicator(color = AppColors.Primary)
        }
    }
}

@Composable
private fun PremiumFeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.Primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
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
private fun PremiumPackageItem(
    premiumPackage: Package,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = premiumPackage.storeProduct.title ?: "Premium Package",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = premiumPackage.storeProduct.localizedDescription ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Secondary
            )
            Text(
                text = premiumPackage.storeProduct.price.formatted,
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.Primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}