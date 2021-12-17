package com.openclassrooms.realestatemanager.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.IOException


inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}

fun savePhoto(
    displayName: String,
    externalUri: Uri,
    context: Context
): Uri? {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(context.contentResolver, externalUri)
        )
    } else {
        MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            externalUri
        )
    }
    val imageCollection = sdk29AndUp {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        sdk29AndUp {
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + "RealEstateManager"
            )
        }
    }
    return try {
        context.contentResolver.insert(imageCollection, contentValues)
            ?.also { uri ->
                context.contentResolver.openOutputStream(uri)
                    .use { outputStream ->
                        if (!bitmap.compress(
                                Bitmap.CompressFormat.JPEG,
                                95,
                                outputStream
                            )
                        ) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                return uri
            } ?: throw IOException("Couldn't create MediaStore entry")
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}


fun deletePhoto(
    context: Context, uri: Uri
): Int {
    return try {
        context.contentResolver.delete(uri, null, null)
    } catch (e: IOException) {
        e.printStackTrace()
        0
    }
}