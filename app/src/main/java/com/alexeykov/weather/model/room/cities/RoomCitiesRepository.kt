package com.alexeykov.weather.model.room.cities

import android.util.Log
import com.alexeykov.weather.model.data.WeatherData
import com.alexeykov.weather.model.data.WeatherShortData
import com.alexeykov.weather.model.room.CitiesRepository
import com.alexeykov.weather.model.room.wrapSQLiteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCitiesRepository(
    private val citiesDao: CitiesDao,
    private val ioDispatcher: CoroutineDispatcher,
) : CitiesRepository {

    override suspend fun addCity(weatherData: WeatherData) = wrapSQLiteException(ioDispatcher) {
        val entity = CitiesDbEntity.fromWeatherData(weatherData)
        citiesDao.addCity(entity)
    }

    override suspend fun deleteCity(cityName: String) {
        citiesDao.deleteCity(cityName)
    }

    override suspend fun changeFavorite(cityName: String, isFavorite: Int) {
        citiesDao.changeFavorite(cityName, isFavorite)
    }

    override suspend fun getAllDataFlow(): Flow<List<WeatherData>> {
        return citiesDao.getAllCityWeatherFlow().map {
            it.map { citiesDbEntity ->
                citiesDbEntity.toWeatherData()
            }
        }
    }

    override suspend fun getShortDataFlow(): Flow<List<WeatherShortData>> {
        return citiesDao.getAllCityWeatherShortFlow().map {
            it.map { cityShortTuple ->
                cityShortTuple.toWeatherShortData()
            }
        }
    }

    override suspend fun getShortData(): List<WeatherShortData> {
        return citiesDao.getAllCityWeatherShort().map { cityShortTuple ->
            cityShortTuple.toWeatherShortData()
        }
    }

    override suspend fun getCity(cityName: String): WeatherData? {
        val weatherData = citiesDao.getCityWeather(cityName)
        return weatherData?.toWeatherData()
    }

    override suspend fun updateWeather(weatherData: WeatherData) {
        citiesDao.updateCity(CitiesDbEntity.fromWeatherData(weatherData))
        Log.d("UpdateCity", weatherData.toString())
    }


}