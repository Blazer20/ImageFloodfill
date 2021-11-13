package com.example.smartstaffsolutiontask.viewmodel.model

interface UserSettingsInterface {
    fun saveUserSettings(us: UserSettingsData)
    fun loadUserSettings(): UserSettingsData
}