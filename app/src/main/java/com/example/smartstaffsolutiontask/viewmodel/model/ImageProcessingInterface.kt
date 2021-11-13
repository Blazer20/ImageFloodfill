package com.example.smartstaffsolutiontask.viewmodel.model

import android.graphics.Bitmap
import android.graphics.Color
import com.example.smartstaffsolutiontask.algorithms.Algorithms
import com.example.smartstaffsolutiontask.algorithms.AlgorithmsDescription

interface ImageProcessingInterface {
    fun generateImage(width: Int, height: Int): Bitmap
    fun imagePart(
            image: Bitmap,
            x: Int, y: Int,
            color: Int = Color.RED,
            algorithm: Algorithms = Algorithms.QUEUE
    ): AlgorithmsDescription
}