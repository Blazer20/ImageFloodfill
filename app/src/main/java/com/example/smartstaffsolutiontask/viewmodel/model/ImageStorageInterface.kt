package com.example.smartstaffsolutiontask.viewmodel.model

import android.graphics.Bitmap

interface ImageStorageInterface {
    fun save(image: Bitmap): Boolean
    fun load(): Bitmap
}