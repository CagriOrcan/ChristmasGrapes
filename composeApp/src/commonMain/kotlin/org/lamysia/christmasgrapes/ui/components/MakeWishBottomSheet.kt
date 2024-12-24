package org.lamysia.christmasgrapes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lamysia.christmasgrapes.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeWishBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var wishText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AppColors.Background,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Make a Wish",
                style = MaterialTheme.typography.headlineSmall,
                color = AppColors.Primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = wishText,
                onValueChange = { wishText = it },
                label = { Text("Enter your wish") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.Primary,
                    focusedLabelColor = AppColors.Primary
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSave(wishText) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                Text("Save Wish")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}