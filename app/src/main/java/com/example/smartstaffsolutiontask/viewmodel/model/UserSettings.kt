package com.example.smartstaffsolutiontask.viewmodel.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.smartstaffsolutiontask.algorithms.Algorithms

class UserSettings(context: Context): UserSettingsInterface {

    companion object {
        private const val PREFS_NAME = "USER_SETTINGS"
        private const val PREFS_FPS_ARG = "FPS"
        private const val PREFS_ALGORITHM_ARG = "ALGORITHM"
    }

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUserSettings(us: UserSettingsData) {
        sharedPreferences.edit {
            putString(PREFS_ALGORITHM_ARG, us.algorithm.name)
            putInt(PREFS_FPS_ARG, us.fps)
        }
    }

    override fun loadUserSettings() = UserSettingsData(
            fps = sharedPreferences.getInt(PREFS_FPS_ARG, 30),
            algorithm = Algorithms.valueOf(
                    sharedPreferences.getString(PREFS_ALGORITHM_ARG, Algorithms.QUEUE.name)!!
            )
    )
}