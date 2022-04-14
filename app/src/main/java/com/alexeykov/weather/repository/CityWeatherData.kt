package com.alexeykov.weather.repository

import kotlin.math.roundToInt

data class CityWeather(
    val id: Long,
    val name: String,
    val coord: Coordinates,
    val base: String,
    val visibility: Int,
    val weather: List<Weather>,
    val main: WeatherMain,
    val wind: Wind,
) {
    fun getTemp(): String = if (main.temp - 273.15f.roundToInt() > 0)
        "+${(main.temp - 273.15f).roundToInt()} °C"
    else
        "${(main.temp - 273.15f).roundToInt()} °C"
    fun getTempFeels(): String = if (main.feels_like - 273.15f.roundToInt() > 0)
        " +${(main.feels_like - 273.15f).roundToInt()} °C. "
    else
        " ${(main.feels_like - 273.15f).roundToInt()} °C. "
    fun getHumidity():String = "${main.humidity}%"
    fun getWeatherId():String = weather[0].id
    fun getWeatherIconId():String = weather[0].icon
    fun getSign():String = "${visibility/1000f}"
}

data class ForecastList(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<Weather>
) {
    fun getTemp(): String = if (main.temp - 273.15f.roundToInt() > 0)
        "+${(main.temp - 273.15f).roundToInt()} °C"
    else
        "${(main.temp - 273.15f).roundToInt()} °C"
    fun getWeatherIconId():String = weather[0].icon
}

data class Weather(
    val id: String,
    val icon: String,
)

data class WeatherMain(
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
)

data class Wind(
    val speed: String,
    val deg: Int,
)

data class Coordinates(
    val lat: String,
    val lon: String,
)