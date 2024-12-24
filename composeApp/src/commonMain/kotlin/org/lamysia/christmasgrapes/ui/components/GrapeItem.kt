package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.theme.AppColors

@Preview
@Composable
fun GrapeItem(
    wish: Wish,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (wish.isLocked) AppColors.Surface else AppColors.Primary)
            .clickable(enabled = !wish.isLocked) { onClick() }
    ) {
        if (wish.isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = AppColors.Primary,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        } else if (wish.hasWish) {
            Text(
                text = "✨",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            )
        }
    }
}