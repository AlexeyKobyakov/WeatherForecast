package com.alexeykov.weather

import android.content.Context
import androidx.room.Room
import com.alexeykov.weather.model.cloud.ApiFactory
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.room.AppDatabase
import com.alexeykov.weather.model.room.CitiesRepository
import com.alexeykov.weather.model.room.cities.RoomCitiesRepository
import java.util.LinkedHashMap

object Repositories {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "database.db").build()
    }

    val localRepository: CitiesRepository by lazy {
        RoomCitiesRepository(database.getCitiesDao())
    }

    val cloudRepository: WeatherRepository = WeatherRepository(ApiFactory.weatherApi)

//    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    val weather: LinkedHashMap<String, String> by lazy {
        getWeatherMap()
    }

    val iconsLink: LinkedHashMap<String, String> by lazy {
        getIconsMap()
    }

    val wind: ArrayList<String> by lazy {
        appContext.resources.getStringArray(R.array.winds).toCollection(ArrayList())
    }

    private fun getIconsMap(): LinkedHashMap<String, String> {
        val iconsArray = appContext.resources.getStringArray(R.array.icons_array)
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(iconsArray.size - 1)
        iconsArray.forEach {
            val str: List<String> = it.split("|")
            linkedHashMap[str[0]] = str[1]
        }
        return linkedHashMap
    }

    private fun getWeatherMap(): LinkedHashMap<String, String> {
        val arrayKeys = appContext.resources.getStringArray(R.array.weather_keys)
        val arrayValues = appContext.resources.getStringArray(R.array.weather_values)
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(arrayKeys.size)
        arrayKeys.indices.forEach {
            linkedHashMap[arrayKeys[it]] = arrayValues[it]
        }
        return linkedHashMap
    }
}