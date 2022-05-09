package com.alexeykov.weather

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ResourcesData.init(this)

        val sPref = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        val theme = sPref.getInt(THEME, AppCompatDelegate.MODE_NIGHT_NO)

        if (theme == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    companion object {

        const val PREFERENCES: String = "main_preferences"
        const val THEME: String = "app_theme"
    }
}