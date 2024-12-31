package org.lamysia.christmasgrapes.ui.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.core.content.FileProvider
import org.lamysia.christmasgrapes.di.PlatformContext
import org.lamysia.christmasgrapes.model.Wish
import java.io.File
import java.io.FileOutputStream

actual class ShareUtil actual constructor(private val platformContext: PlatformContext?) {
    actual suspend fun shareWish(wishText: String) {
        platformContext?.context?.let { context ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, """
                    🌟 My New Year's Wish 🌟
                    
                    $wishText
                    
                    Made with Christmas Grapes App ✨
                """.trimIndent())
            }
            context.startActivity(Intent.createChooser(intent, "Share your wish"))
        }
    }

    actual suspend fun captureAndSharePostcard(wish: Wish) {
        platformContext?.context?.let { context ->
            val bitmap = Bitmap.createBitmap(1080, 1620, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Background çiz
           /* val backgroundBitmap = BitmapFactory.decodeResource(
                context.resources,
                Res.drawable.postcard
            )
            canvas.drawBitmap(
                backgroundBitmap,
                null,
                RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat()),
                null
            )
*/
            // Text çiz
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 48f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create("cursive", Typeface.NORMAL)
            }

            // Metni satırlara böl ve ortala
            val textBounds = Rect()
            paint.getTextBounds(wish.text, 0, wish.text.length, textBounds)

            val textLines = wish.text.split("\n")
            val lineHeight = textBounds.height() * 1.5f
            val totalTextHeight = lineHeight * textLines.size
            var y = (bitmap.height - totalTextHeight) / 2f

            textLines.forEach { line ->
                canvas.drawText(
                    line,
                    bitmap.width / 2f,
                    y + textBounds.height(),
                    paint
                )
                y += lineHeight
            }

            // Share bitmap
            val imagesFolder = File(context.cacheDir, "images")
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "postcard.png")

            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share your wish"))

            // Cleanup
           // backgroundBitmap.recycle()
            bitmap.recycle()
        }
    }
}
