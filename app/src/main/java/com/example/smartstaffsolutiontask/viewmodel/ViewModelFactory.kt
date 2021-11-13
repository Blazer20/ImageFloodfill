package com.example.smartstaffsolutiontask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstaffsolutiontask.viewmodel.model.*

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val imageStorage: ImageStorageInterface,
    private val imageProcessing: ImageProcessingInterface,
    private val floodFiller: FloodfillerInterface,
    private val userSettings: UserSettingsInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(
                imageStorage,
                imageProcessing,
                floodFiller,
                userSettings
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}