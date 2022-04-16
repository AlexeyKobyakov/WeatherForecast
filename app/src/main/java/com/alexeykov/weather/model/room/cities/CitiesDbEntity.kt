package com.alexeykov.weather.model.room.cities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.alexeykov.weather.model.data.WeatherData

@Entity(
    tableName = "cities",
    indices = [
        Index("city_name", unique = true)
    ]
)
data class CitiesDbEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "coordinate_lat") val coordLat: String,
    @ColumnInfo(name = "coordinates_lon") val coordLon: String,
    @ColumnInfo(name = "temperature") val temperature: String,
    @ColumnInfo(name = "temp_feel") val tempFeel: String,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: String,
    @ColumnInfo(name = "visibility") val visibility: String,
    @ColumnInfo(name = "weather") val weather: String,
    @ColumnInfo(name = "icon_link") val iconLink: String,
    @ColumnInfo(name = "wind_speed") val windSpeed: Float,
    @ColumnInfo(name = "wind_deg") val windDeg: Int,
) {

    fun toWeatherData(): WeatherData = WeatherData(
        id = id,
        cityName = cityName,
        isFavorite = isFavorite,
        coordLat = coordLat,
        coordLon = coordLon,
        temperature = temperature,
        tempFeel = tempFeel,
        pressure = pressure,
        humidity = humidity,
        visibility = visibility,
        weather = weather,
        iconLink = iconLink,
        windSpeed = windSpeed,
        windDeg = windDeg,
    )

    companion object {
        fun fromWeatherData(weatherData: WeatherData): CitiesDbEntity = CitiesDbEntity(
            id = weatherData.id,
            cityName = weatherData.cityName,
            isFavorite = weatherData.isFavorite,
            coordLat = weatherData.coordLat,
            coordLon = weatherData.coordLon,
            temperature = weatherData.temperature,
            tempFeel = weatherData.tempFeel,
            pressure = weatherData.pressure,
            humidity = weatherData.humidity,
            visibility = weatherData.visibility,
            weather = weatherData.weather,
            iconLink = weatherData.iconLink,
            windSpeed = weatherData.windSpeed,
            windDeg = weatherData.windDeg,
        )
    }

}
