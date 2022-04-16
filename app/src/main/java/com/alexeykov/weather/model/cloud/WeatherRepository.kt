package com.alexeykov.weather.model.cloud

import com.alexeykov.weather.model.data.CityWeather
import com.alexeykov.weather.model.data.ForecastList

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