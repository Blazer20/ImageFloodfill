package com.example.smartstaffsolutiontask.viewmodel.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ImageStorage(context: Context): ImageStorageInterface {
    companion object {
        private const val IMAGE_NAME = "image.png"
    }

    private val imageFile = File(context.filesDir, IMAGE_NAME)

    override fun save(image: Bitmap): Boolean {
        try {
            FileOutputStream(imageFile).use { out ->
                image.compress(Bitmap.CompressFormat.PNG, 100, out)
                return true
            }
        } catch (e: IOException){
            System.err.println(e)
        }
        return false
    }

    override fun load(): Bitmap {
        val decodeStream = BitmapFactory.decodeStream(FileInputStream(imageFile))
        val bitmap = decodeStream.copy(Bitmap.Config.RGB_565, true)
        decodeStream.recycle()
        return bitmap
    }
}