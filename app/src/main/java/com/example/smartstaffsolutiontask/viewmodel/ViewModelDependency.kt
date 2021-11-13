package com.example.smartstaffsolutiontask.viewmodel

import android.content.Context
import com.example.smartstaffsolutiontask.viewmodel.model.*

class ViewModelDependency(val context: Context) {

    val imageStorage: ImageStorageInterface
            by lazy {
                ImageStorage(context = context)
            }

    val imageProcessing: ImageProcessingInterface
            by lazy {
                ImageProcessing()
            }

    val floodFiller: FloodfillerInterface
            by lazy {
                Floodfiller(context = context)
            }

    val userSettings: UserSettingsInterface
            by lazy {
                UserSettings(context = context)
            }

    val viewModelFactory: ViewModelFactory
            by lazy {
                ViewModelFactory(
                        imageStorage,
                        imageProcessing,
                        floodFiller,
                        userSettings
                )
            }
}