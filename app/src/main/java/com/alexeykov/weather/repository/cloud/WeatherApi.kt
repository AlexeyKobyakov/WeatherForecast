package com.alexeykov.weather.repository.cloud

import com.alexeykov.weather.repository.CityWeather
import com.alexeykov.weather.repository.ForecastList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCityWeather(
        @Query("q") city: String
    ): Response<CityWeather>

    @GET("forecast")
    suspend fun getForecastFiveDays(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<ForecastList>
}