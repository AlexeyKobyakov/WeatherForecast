package com.alexeykov.weather.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alexeykov.weather.R
import com.alexeykov.weather.adapters.CityListAdapter
import com.alexeykov.weather.model.NoNetworkConnection
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.data.CloudToLocalData
import com.alexeykov.weather.model.data.WeatherShortData
import com.alexeykov.weather.model.room.CitiesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val localRepository: CitiesRepository,
    private val cloudRepository: WeatherRepository,
    private val navController: NavController,
) : ViewModel(), CityListAdapter.Listener {


    private var _items: MutableLiveData<List<WeatherShortData>> = MutableLiveData()
    var items: LiveData<List<WeatherShortData>> = _items

    private var _delete: MutableLiveData<String> = MutableLiveData("")
    var delete: LiveData<String> = _delete

    private var _errors: MutableLiveData<Int> = MutableLiveData<Int>()
    var errors: LiveData<Int> = _errors

    private var _update: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var update: LiveData<Boolean> = _update

    private val job = CoroutineScope(Dispatchers.IO)

    init {
        getWeatherInCities()
        getForecast()
    }

    fun getForecast() {
        _update.postValue(true)
        job.launch {
            val citiesList = localRepository.getShortData()
            citiesList.forEach { weatherShortData ->
                try {
                    val forecast = cloudRepository.getWeatherInCity(weatherShortData.cityName)
                    forecast?.let {
                        val weather = CloudToLocalData.getWeatherData(
                            cityId = weatherShortData.id,
                            cityName = weatherShortData.cityName,
                            isFavorite = weatherShortData.isFavorite,
                            cityWeather = it
                        )
                        localRepository.updateWeather(weather)
                    }
                } catch (e: NoNetworkConnection) {
                    _errors.postValue(R.string.error_no_internet)
                }
            }
            _update.postValue(false)
        }
    }

    private fun getWeatherInCities() {
        job.launch {
            localRepository.getShortDataFlow().collect {
                _items.postValue(it)
            }
        }
    }

    fun navigateToAddCity() {
        navController.navigate(R.id.action_MainFragment_to_AddCityFragment)
    }

    override fun onItemClicked(cityName: String) {
        val bundle = Bundle().apply { putString("city", cityName) }
        navController.navigate(R.id.action_MainFragment_to_DetailsFragment, bundle)
    }

    override fun onDeleteClicked(cityName: String) {
        _delete.postValue(cityName)
    }

    fun deleteCity(cityName: String) {
        job.launch {
            localRepository.deleteCity(cityName)
            _delete.postValue("")
        }
    }

    override fun onFavoriteClicked(item: WeatherShortData) {
        job.launch {
            val isFavorite = if (item.isFavorite == 0) 1 else 0
            localRepository.changeFavorite(item.cityName, isFavorite)
        }
    }

    fun clearDelete() {
        _delete.postValue("")
    }
}