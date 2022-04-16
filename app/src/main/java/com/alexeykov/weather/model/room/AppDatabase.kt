package com.alexeykov.weather.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexeykov.weather.model.room.cities.CitiesDao
import com.alexeykov.weather.model.room.cities.CitiesDbEntity

@Database(
    version = 1,
    entities = [
        CitiesDbEntity::class
    ]
)
abstract  class AppDatabase: RoomDatabase() {

    abstract fun getCitiesDao(): CitiesDao
}