package com.alexeykov.weather.model.cloud

import com.alexeykov.weather.model.data.CityWeather
import com.alexeykov.weather.model.data.ForecastList
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