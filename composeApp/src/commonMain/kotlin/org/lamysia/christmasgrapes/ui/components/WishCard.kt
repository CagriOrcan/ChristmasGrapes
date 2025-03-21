package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.postcard
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.ui.theme.AppColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WishCard(
    wish: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Postcard arka plan
            Image(
                painter = painterResource(Res.drawable.postcard),
                contentDescription = "Postcard",
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Dilek metni
            androidx.compose.material3.Text(
                text = wish,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp
                ),
                color = AppColors.Primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(48.dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}