package com.example.smartstaffsolutiontask.viewmodel.model

import com.example.smartstaffsolutiontask.algorithms.Algorithms

data class UserSettingsData(
        val fps: Int = 30,
        val algorithm: Algorithms = Algorithms.QUEUE
)
