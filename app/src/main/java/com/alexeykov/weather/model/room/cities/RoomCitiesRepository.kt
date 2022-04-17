package com.alexeykov.weather.model.room.cities

import com.alexeykov.weather.model.data.WeatherData
import com.alexeykov.weather.model.data.WeatherShortData
import com.alexeykov.weather.model.room.CitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCitiesRepository(
    private val citiesDao: CitiesDao,
//    private val ioDispatcher: CoroutineDispatcher,
): CitiesRepository {

    override suspend fun addCity(weatherData: WeatherData) {
        val entity = CitiesDbEntity.fromWeatherData(weatherData)
        citiesDao.addCity(entity)
    }

    override suspend fun deleteCity(cityName: String) {
        citiesDao.deleteCity(cityName)
    }

    override suspend fun changeFavorite(cityName: String, isFavorite: Int) {
        citiesDao.changeFavorite(cityName, isFavorite)
    }

    override suspend fun getAllData(): Flow<List<WeatherData>> {
        return citiesDao.getAllCityWeather().map {
            it.map { citiesDbEntity ->
                citiesDbEntity.toWeatherData()
            }
        }
    }

    override suspend fun getShortData(): Flow<List<WeatherShortData>> {
        return citiesDao.getAllCityWeatherShort().map {
            it.map { cityShortTuple ->
                cityShortTuple.toWeatherShortData()
            }
        }
    }

    override suspend fun getCity(name: String): WeatherData? {
        val weatherData = citiesDao.getCityWeather(name)
        return weatherData?.toWeatherData()
    }

    override suspend fun updateWeather(weatherData: WeatherData) {
        citiesDao.updateCity(CitiesDbEntity.fromWeatherData(weatherData))
    }


}