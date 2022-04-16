package com.alexeykov.weather

import android.app.Application

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Repositories.init(this)
    }

}