package com.ninecraft.booket.core.common.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.orhanobut.logger.Logger
import java.io.File

@Suppress("TooGenericExceptionCaught")
fun Context.externalShareForBitmap(bitmap: ImageBitmap) {
    try {
        val file = File(bitmap.saveToDisk(this))
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

        ShareCompat.IntentBuilder(this)
            .setStream(uri)
            .setType("image/png")
            .startChooser()
    } catch (e: Exception) {
        Logger.e("[externalShareFoBitmap] message: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.bitmapToFileUri(bitmap: ImageBitmap): Uri? {
    return try {
        val file = File(bitmap.saveToDisk(this))
        FileProvider.getUriForFile(this, "$packageName.provider", file)
    } catch (e: Exception) {
        Logger.e("Failed to convert bitmap to URI: ${e.message}")
        null
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.shareImage(imageUri: Uri) {
    try {
        ShareCompat.IntentBuilder(this)
            .setStream(imageUri)
            .setType("image/png")
            .startChooser()
    } catch (e: Exception) {
        Logger.e("Failed to share image: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.saveImageToGallery(bitmap: ImageBitmap) {
    try {
        val fileName = "bandalart_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        }
    } catch (e: Exception) {
        Logger.e("Failed to save image to gallery: ${e.message}")
    }
}

@Suppress("TooGenericExceptionCaught")
fun Context.saveUriToGallery(imageUri: Uri) {
    try {
        val fileName = "bandalart_${System.currentTimeMillis()}.png"
        val contentValues = createContentValues(fileName)
        val destinationUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return

        copyUriContent(imageUri, destinationUri)
        updatePendingStatus(destinationUri, contentValues)
    } catch (e: Exception) {
        Logger.e("Failed to save image to gallery: ${e.message}")
    }
}

private fun createContentValues(fileName: String) = ContentValues().apply {
    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }
}

private fun Context.copyUriContent(sourceUri: Uri, destinationUri: Uri) {
    contentResolver.openInputStream(sourceUri)?.use { input ->
        contentResolver.openOutputStream(destinationUri)?.use { output ->
            input.copyTo(output)
        }
    }
}

private fun Context.updatePendingStatus(uri: Uri, contentValues: ContentValues) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, contentValues, null, null)
    }
}
