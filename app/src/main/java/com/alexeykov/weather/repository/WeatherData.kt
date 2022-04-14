package com.alexeykov.weather.repository

data class WeatherData(
    val id: Long,
    val cityName: String,
    val isFavorite: Boolean = false,
    val coordLat: String,
    val coordLon: String,
    val temperature: String,
    val temp_feel: String,
    val pressure: Int,
    val humidity: String,
    val visibility: String,
    val weather: String,
    val iconLink: String,
    val windSpeed: Float,
    val windDeg: Int
)

