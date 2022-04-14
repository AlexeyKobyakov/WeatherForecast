package com.alexeykov.weather.repository.cloud

import com.alexeykov.weather.repository.CityWeather
import com.alexeykov.weather.repository.ForecastList

class WeatherRepository(private val api : WeatherApi) : BaseRepository() {

    suspend fun getWeatherInCity(name: String): CityWeather? {
        return safeApiCall(
            call = { api.getCityWeather(name) },
            errorMessage = "Error fetching weather in city $name"
        )
    }

    suspend fun getForecast(lat: String, lon: String): ForecastList? {
        return safeApiCall(
            call = { api.getForecastFiveDays(lat, lon) },
            errorMessage = "Error fetching forecast for 5 days"
        )
    }
}