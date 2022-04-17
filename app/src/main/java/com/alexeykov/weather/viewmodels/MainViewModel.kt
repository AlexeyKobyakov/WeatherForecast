package com.alexeykov.weather.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.alexeykov.weather.R
import com.alexeykov.weather.adapters.CityListAdapter
import com.alexeykov.weather.model.cloud.WeatherRepository
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

    private val job = CoroutineScope(Dispatchers.IO)

    init {
        getWeatherInCities()
    }

    private fun getWeatherInCities() {
        job.launch {
            localRepository.getShortData().collect {
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
        job.launch {
            localRepository.deleteCity(cityName)
        }
    }

    override fun onFavoriteClicked(item: WeatherShortData) {
        job.launch {
            val isFavorite = if (item.isFavorite == 0) 1 else 0
            localRepository.changeFavorite(item.cityName, isFavorite)
        }
    }
}