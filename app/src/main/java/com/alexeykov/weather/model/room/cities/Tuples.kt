package com.alexeykov.weather.model.room.cities

import androidx.room.ColumnInfo
import com.alexeykov.weather.model.data.WeatherShortData

data class CityShortTuple(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "temperature") val temperature: String,
    @ColumnInfo(name = "icon_link") val iconLink: String,
) {
    fun toWeatherShortData(): WeatherShortData = WeatherShortData(
        id = id,
        cityName = cityName,
        temperature = temperature,
        iconLink = iconLink,
    )
}