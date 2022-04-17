package com.alexeykov.weather.model.data

data class WeatherData(
    val id: Int,
    val cityName: String,
    val isFavorite: Int,
    val coordLat: String,
    val coordLon: String,
    val temperature: String,
    val tempFeel: String,
    val pressure: Int,
    val humidity: String,
    val visibility: String,
    val weather: String,
    val iconLink: String,
    val windSpeed: Float,
    val windDeg: Int
)

