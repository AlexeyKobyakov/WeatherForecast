package com.alexeykov.weather.model.room

import com.alexeykov.weather.model.data.WeatherData
import com.alexeykov.weather.model.data.WeatherShortData
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    suspend fun addCity(weatherData: WeatherData)

    suspend fun deleteCity(cityName: String)

    suspend fun changeFavorite(cityName: String, isFavorite: Int)

    suspend fun getAllData(): Flow<List<WeatherData>>

    suspend fun getShortData(): Flow<List<WeatherShortData>>

    suspend fun getCity(cityName: String): WeatherData?

    suspend fun updateWeather(weatherData: WeatherData)

}