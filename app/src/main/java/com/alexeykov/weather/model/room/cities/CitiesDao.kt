package com.alexeykov.weather.model.room.cities

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {

    @Insert(entity = CitiesDbEntity::class)
    suspend fun addCity(citiesDbEntity: CitiesDbEntity)

    @Update(entity = CitiesDbEntity::class)
    suspend fun updateCity(citiesDbEntity: CitiesDbEntity)

    @Query("DELETE FROM cities WHERE id = :id")
    fun deleteCity(id: Int)

    @Query("SELECT * FROM cities WHERE city_name = :name ORDER BY is_favorite")
    suspend fun getCityWeather (name: String): CitiesDbEntity?

    @Query("SELECT id, city_name, temperature, icon_link FROM cities ORDER BY is_favorite")
    fun getAllCityWeatherShort (): Flow<List<CityShortTuple>>

    @Query("SELECT * FROM cities ORDER BY is_favorite")
    fun getAllCityWeather (): Flow<List<CitiesDbEntity>>
}