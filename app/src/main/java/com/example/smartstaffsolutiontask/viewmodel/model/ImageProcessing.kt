package com.example.smartstaffsolutiontask.viewmodel.model

import android.graphics.Bitmap
import android.graphics.Point
import com.example.smartstaffsolutiontask.algorithms.Algorithms
import com.example.smartstaffsolutiontask.algorithms.AlgorithmsDescription
import com.example.smartstaffsolutiontask.generate.GeneratePictures
import java.lang.Math.random
import kotlin.math.cos

class ImageProcessing : ImageProcessingInterface {
    override fun generateImage(width: Int, height: Int): Bitmap {
        val seed = (
                (System.nanoTime() + (cos((width and height).toFloat()) * 1000)) * random()
                ).toLong()
        return GeneratePictures().generate(width, height, seed)
    }

    override fun imagePart(image: Bitmap, x: Int, y: Int, color: Int, algorithm: Algorithms): AlgorithmsDescription {
        return AlgorithmsDescription.create(image, Point(x, y), color, algorithm)
    }
}