package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import christmasgrapes.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.di.LocalPlatformContext
import org.lamysia.christmasgrapes.model.Wish
import org.lamysia.christmasgrapes.ui.animations.ShareAnimationEffect
import org.lamysia.christmasgrapes.ui.animations.ShareAnimationState
import org.lamysia.christmasgrapes.ui.theme.AppColors
import org.lamysia.christmasgrapes.ui.utils.ShareUtil

@OptIn(ExperimentalResourceApi::class)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, bottom = 48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.postcard),
                contentDescription = "Postcard",
                modifier = Modifier
                    .fillMaxWidth()
            )

            ShareAnimationEffect(
                state = animationState,
                modifier = Modifier.fillMaxSize()
            )
            if (showShareButton) {
                IconButton(
                    onClick = {
                        scope.launch {
                            try {
                                shareUtil.captureAndSharePostcard(wish)
                            } catch (e: Exception) {
                                // Fallback to text share if image share fails
                                shareUtil.shareWish(wish.text)
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.share),
                        contentDescription = "Share wish"
                    )
                }
            }

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = AppColors.Primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        text = "Failed to generate wish",
                        color = AppColors.Error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = wish.text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.Cursive,
                                fontSize = 24.sp
                            ),
                            color = AppColors.Primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(onClick = onDismiss) {
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
        }
    }
}