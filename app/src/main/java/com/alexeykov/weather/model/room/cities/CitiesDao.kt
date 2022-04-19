package com.alexeykov.weather.model.room.cities

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {

    @Insert(entity = CitiesDbEntity::class)
    suspend fun addCity(citiesDbEntity: CitiesDbEntity)

    @Update(entity = CitiesDbEntity::class)
    suspend fun updateCity(citiesDbEntity: CitiesDbEntity)

    @Query("DELETE FROM cities WHERE city_name = :name")
    suspend fun deleteCity(name: String)

    @Query("UPDATE cities SET is_favorite = :isFavorite WHERE city_name = :cityName")
    suspend fun changeFavorite(cityName: String, isFavorite: Int)

    @Query("SELECT * FROM cities WHERE city_name = :name")
    suspend fun getCityWeather(name: String): CitiesDbEntity?

    @Query("SELECT id, city_name, is_favorite, RTRIM(temperature, ' °C') as temperature, icon_link FROM cities ORDER BY is_favorite DESC, id")
    fun getAllCityWeatherShortFlow(): Flow<List<CityShortTuple>>

    @Query("SELECT id, city_name, is_favorite, RTRIM(temperature, ' °C') as temperature, icon_link FROM cities ORDER BY is_favorite DESC, id")
    suspend fun getAllCityWeatherShort(): List<CityShortTuple>

    @Query("SELECT * FROM cities ORDER BY is_favorite DESC, id")
    fun getAllCityWeatherFlow(): Flow<List<CitiesDbEntity>>

}