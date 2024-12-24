package org.lamysia.christmasgrapes.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = AppColors.Primary,
            secondary = AppColors.Secondary,
            background = AppColors.Background,
            surface = AppColors.Surface,
            error = AppColors.Error
        ),
        content = content
    )
}