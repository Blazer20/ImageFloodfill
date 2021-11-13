package com.example.smartstaffsolutiontask.viewmodel.model

import android.graphics.Bitmap
import com.example.smartstaffsolutiontask.algorithms.AlgorithmsDescription

interface FloodfillerInterface {
    fun saveFloodFillers(list: List<AlgorithmsDescription>)
    fun getFloodFillers(image: Bitmap): List<AlgorithmsDescription>
}