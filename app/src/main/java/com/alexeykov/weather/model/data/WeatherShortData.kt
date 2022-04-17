package com.alexeykov.weather.model.data

data class WeatherShortData (
    val id: Int,
    val isFavorite: Int,
    val cityName: String,
    val temperature: String,
    val iconLink: String,
)