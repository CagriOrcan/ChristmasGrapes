package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.postcard
import christmasgrapes.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lamysia.christmasgrapes.di.LocalPlatformContext
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.animations.ShareAnimationState
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.utils.ShareUtil

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun WishDialog(
    wish: Wish,
    isLoading: Boolean = false,
    error: String? = null,
    onDismiss: () -> Unit,
    onSave: (Wish) -> Unit,
    showShareButton: Boolean = false
) {
    var animationState by remember { mutableStateOf<ShareAnimationState>(ShareAnimationState.None) }
    val platformContext = LocalPlatformContext.current
    val shareUtil = remember { ShareUtil(platformContext) }
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.postcard),
                    contentDescription = "Postcard",
                    modifier = Modifier.fillMaxWidth()
                )

                // Share Button in Top Right Corner
                if (showShareButton) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(40.dp)
                            .border(2.dp, AppColors.Primary, CircleShape)
                            .background(color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        shareUtil.captureAndSharePostcard(wish)
                                    } catch (e: Exception) {
                                        shareUtil.shareWish(wish.text)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(Res.drawable.share),
                                contentDescription = "Share wish",
                                tint = AppColors.Primary
                            )
                        }
                    }
                }

                // Wish Text in Center
                Text(
                    text = wish.text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Cursive,
                        fontSize = 32.sp
                    ),
                    color = AppColors.Primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp)
                )
            }

            // Close and Save Wish Buttons Directly Below Postcard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // 16.dp below the postcard
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
               Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = AppColors.Secondary
                    )
                ) {
                    Text("Close")
                }

                Button(
                    onClick = { onSave(wish) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary
                    )
                ) {
                    Text("Save Wish")
                }
            }
        }
    }
}
