package ru.ar4uk.bookstoreapp.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {
    // для Bitmap
    fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String {
        // Получим байты картинки
        val bytes = uriToByteArray(uri, contentResolver)
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

        return base64Image
    }

    // уменьшить размер пикселей
    fun resizeBitmapImage(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width <= maxSize && height <= maxSize) return bitmap

        val imageRatio = width.toFloat() / height.toFloat()

        if (imageRatio > 1) { // горизонтальная картинка
            width = maxSize
            height = (width/imageRatio).toInt()
        } else { // вертикальная картинка
            height = maxSize
            width = (height * imageRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    fun uriToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray {
        val inputStream = contentResolver.openInputStream(uri) // получаем поток байтов из хранилища устройства

        val bm = BitmapFactory.decodeStream(inputStream)
        // уменьшаем размер пикселей
        val resizedBitmap = resizeBitmapImage(bm, 300)
        val stream = ByteArrayOutputStream()

        // сжатие картинки
        if (Build.VERSION.SDK_INT >= 30) {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 50, stream)
        } else {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream)
        }

        return stream.toByteArray()
    }
}