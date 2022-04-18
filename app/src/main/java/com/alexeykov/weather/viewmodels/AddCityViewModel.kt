package com.alexeykov.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alexeykov.weather.model.data.CloudToLocalData
import com.alexeykov.weather.R
import com.alexeykov.weather.model.NoNetworkConnection
import com.alexeykov.weather.model.StorageException
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.room.CitiesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCityViewModel(
    private val localRepository: CitiesRepository,
    private val cloudRepository: WeatherRepository,
    private val navController: NavController,
) : ViewModel() {

    private val job: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var _errors: MutableLiveData<Int> = MutableLiveData<Int>()
    var errors: LiveData<Int> = _errors

    fun addCity(name: String) {
        job.launch {
            try {
                saveCity(name)
            } catch (e: StorageException) {
                _errors.postValue(R.string.error_already_exist)
            }
        }
    }

    private suspend fun saveCity(cityName: String) {
        try {
            val cityWeather = cloudRepository.getWeatherInCity(cityName)
            Log.d("AddCityViewModel", cityWeather.toString())
            cityWeather?.let {
                localRepository.addCity(weatherData = CloudToLocalData.getWeatherData(
                    cityId = 0,
                    cityName = cityName,
                    isFavorite = 0,
                    cityWeather = it))
                Log.d("AddCityViewModel", it.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate(R.id.action_AddCityFragment_pop)
                }
            } ?: _errors.postValue(R.string.error_load_data)
        } catch (e: NoNetworkConnection) {
            _errors.postValue(R.string.error_no_internet)
        }
    }

}