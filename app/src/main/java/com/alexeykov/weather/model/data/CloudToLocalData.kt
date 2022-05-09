package com.alexeykov.weather.model.data

import com.alexeykov.weather.ResourcesData

object CloudToLocalData {

    fun getWeatherData(cityId: Int, cityName: String, isFavorite: Int, cityWeather: CityWeather): WeatherData {
        return WeatherData(
            id = cityId,
            cityName = cityName,
            isFavorite = isFavorite,
            coordLat = cityWeather.coord.lat,
            coordLon = cityWeather.coord.lon,
            temperature = cityWeather.getTemp(),
            tempFeel = cityWeather.getTempFeels(),
            pressure = cityWeather.main.pressure,
            humidity = cityWeather.getHumidity(),
            visibility = cityWeather.getSign(),
            weather = ResourcesData.weather[cityWeather.getWeatherId()]!!,
            iconLink = ResourcesData.iconsLink[cityWeather.getWeatherIconId()]!!,
            windSpeed = cityWeather.wind.speed,
            windDeg = cityWeather.wind.deg
        )
    }
}