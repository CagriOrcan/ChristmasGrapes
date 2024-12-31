package org.lamysia.christmasgrapes.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import christmasgrapes.composeapp.generated.resources.Res
import christmasgrapes.composeapp.generated.resources.circular_regular
import christmasgrapes.composeapp.generated.resources.postcard
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.painterResource
import org.lamysia.christmasgrapes.ui.theme.AppColors


data class PostcardDesign(
    val name: String,
    val backgroundRes: DrawableResource,
    val textColor: Color,
    val font: FontResource,
    val previewRes: DrawableResource,
    val shareAnimation: ShareAnimation = ShareAnimation.Fade
) {
    companion object {
        val Default = PostcardDesign(
            name = "Classic",
            backgroundRes = Res.drawable.postcard,
            textColor = Color(0xFF722F37),
            font = Res.font.circular_regular,
            previewRes =  Res.drawable.postcard,
        )

        val Collection = listOf(
            Default,
            PostcardDesign(
                name = "Winter",
                backgroundRes = Res.drawable.postcard,
                textColor = Color.White,
                font = Res.font.circular_regular,
                previewRes = Res.drawable.postcard,
                shareAnimation = ShareAnimation.Snow
            ),
            PostcardDesign(
                name = "Golden",
                backgroundRes = Res.drawable.postcard,
                textColor = Color(0xFF614E1A),
                font = Res.font.circular_regular,
                previewRes = Res.drawable.postcard,
                shareAnimation = ShareAnimation.Sparkle
            )
        )
    }
}

enum class ShareAnimation {
    Fade,
    Snow,
    Sparkle,
    Fireworks
}

/*// PostcardSelector composable
@Composable
fun PostcardSelector(
    selectedDesign: PostcardDesign,
    onDesignSelected: (PostcardDesign) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(PostcardDesign.Collection) { design ->
            PostcardPreview(
                design = design,
                isSelected = design == selectedDesign,
                onClick = { onDesignSelected(design) }
            )
        }
    }
}*/

@Composable
private fun PostcardPreview(
    design: PostcardDesign,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(0.75f)
            .clickable(onClick = onClick),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            color = if (isSelected) AppColors.Primary else Color.Transparent
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(design.previewRes),
                contentDescription = design.name,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = design.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
