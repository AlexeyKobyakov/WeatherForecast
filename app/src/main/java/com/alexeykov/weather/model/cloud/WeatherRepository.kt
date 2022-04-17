package com.alexeykov.weather.model.cloud

import com.alexeykov.weather.model.data.CityWeather
import com.alexeykov.weather.model.data.ForecastList
import kotlinx.coroutines.CoroutineDispatcher

class WeatherRepository(
    private val api: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseRepository() {

    suspend fun getWeatherInCity(name: String): CityWeather? = wrapNetworkException(ioDispatcher) {
        return@wrapNetworkException safeApiCall(
            call = { api.getCityWeather(name) },
            errorMessage = "Error fetching weather in city $name"
        )
    }

    suspend fun getForecast(lat: String, lon: String): ForecastList? = wrapNetworkException(ioDispatcher)  {
        return@wrapNetworkException safeApiCall(
            call = { api.getForecastFiveDays(lat, lon) },
            errorMessage = "Error fetching forecast for 5 days"
        )
    }
}