package com.example.smartstaffsolutiontask.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartstaffsolutiontask.algorithms.Algorithms
import com.example.smartstaffsolutiontask.viewmodel.model.*
import java.io.IOException
import kotlin.math.max

class MyViewModel(
        private val imageStorage: ImageStorageInterface,
        private val imageProcessing: ImageProcessingInterface,
        private val floodFiller: FloodfillerInterface,
        private val userSettings: UserSettingsInterface
) : ViewModel() {
    companion object {
        const val FILLING_COLOR: Int  = 0xFF032F01.toInt()
    }

    val height
        get() = image.value?.height ?: 0

    val width
        get() = image.value?.width ?: 0

    val fps = MutableLiveData<Int>()
    val image = MutableLiveData<Bitmap>()
    val algorithms = MutableLiveData<Algorithms>()
    val isBusy = MutableLiveData<Boolean>()

    private val executor = MyExecutor()

    init {
        isBusy.value = true
        try {
            image.value = imageStorage.load()
            try {
                val fillers = floodFiller.getFloodFillers(image.value!!)
                fillers.forEach {
                    executor.addFiller(it)
                }
            } catch (e: IOException) {}

        } catch (e: IOException) {
            System.err.println(e)
            onGenerateImage(100, 100)
        }
        val userSettings = userSettings.loadUserSettings()
        isBusy.value = false
        fps.value = userSettings.fps
        algorithms.value = userSettings.algorithm
    }

    fun onBitmapClicked(x: Int, y: Int) {
        onNewPoint(x, y)
    }

    fun onNewFps(fps: Int) {
        this.fps.value = fps
        updateFps()
    }

    fun onNewAlgorithm(algorithm: Algorithms) {
        this.algorithms.value = algorithm
    }

    fun onGenerateImage(width: Int, height: Int) {
        isBusy.value = true
        executor.clear()
        image.value = generateImage(max(width, 32), max(height, 32))
        saveImage()
        isBusy.value = false
    }

    override fun onCleared() {
        saveImage()
        try {
            image.value?.recycle()
        } catch (e: Exception) {}
        super.onCleared()
    }

    fun onStart() {
        executor.start()
    }

    fun onStop() {
        executor.stop()
        saveImage()
        saveFillers()
        saveSettings()
    }

    private fun onNewPoint(x: Int, y: Int) {
        val floodFiller =
                imageProcessing.imagePart(
                        image.value ?: return,
                        x,
                        y,
                        FILLING_COLOR,
                        algorithms.value ?: return
                )
        updateFps()
        executor.addFiller(floodFiller)
    }

    private fun generateImage(width: Int, height: Int): Bitmap {
        return imageProcessing.generateImage(width, height)
    }

    private fun updateFps() {
        if (fps.value == null) {
            fps.value = 30
        }
        executor.fps = fps.value!!
    }

    private fun saveSettings() {
        userSettings.saveUserSettings(
                UserSettingsData(
                        fps.value!!,
                        algorithms.value!!
                )
        )
    }

    private fun saveImage() {
        imageStorage.save(image.value ?: return)
    }

    private fun saveFillers() {
        floodFiller.saveFloodFillers(executor.getFillers())
    }
}