package com.alexeykov.weather.model.data

import com.alexeykov.weather.Repositories

object CloudToLocalData {

    fun getWeatherData(cityName: String, cityWeather: CityWeather): WeatherData {
        return WeatherData(
            id = 0,
            cityName = cityName,
            isFavorite = 0,
            coordLat = cityWeather.coord.lat,
            coordLon = cityWeather.coord.lon,
            temperature = cityWeather.getTemp(),
            tempFeel = cityWeather.getTempFeels(),
            pressure = cityWeather.main.pressure,
            humidity = cityWeather.getHumidity(),
            visibility = cityWeather.getSign(),
            weather = Repositories.weather[cityWeather.getWeatherId()]!!,
            iconLink = Repositories.iconsLink[cityWeather.getWeatherIconId()]!!,
            windSpeed = cityWeather.wind.speed,
            windDeg = cityWeather.wind.deg
        )
    }
}