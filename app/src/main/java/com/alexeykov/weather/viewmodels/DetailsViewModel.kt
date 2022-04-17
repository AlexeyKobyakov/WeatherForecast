package com.alexeykov.weather.viewmodels

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexeykov.weather.R
import com.alexeykov.weather.model.data.CloudToLocalData
import com.alexeykov.weather.Repositories
import com.alexeykov.weather.model.NoNetworkConnection
import com.alexeykov.weather.model.data.WeatherData
import com.alexeykov.weather.model.data.ForecastData
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.room.CitiesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DetailsViewModel(
    private val localRepository: CitiesRepository,
    private val cloudRepository: WeatherRepository,
) : ViewModel() {

    private var _weatherData: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var weatherData: LiveData<WeatherData> = _weatherData

    private var _day1Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day1Data: LiveData<ForecastData> = _day1Data

    private var _day2Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day2Data: LiveData<ForecastData> = _day2Data

    private var _day3Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day3Data: LiveData<ForecastData> = _day3Data

    private var _errors: MutableLiveData<Int> = MutableLiveData<Int>()
    var errors: LiveData<Int> = _errors

    private val job: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun getWeather(cityName: String) {
        job.launch {
            try {
                val response = cloudRepository.getWeatherInCity(cityName)
                response?.let { cityWeather ->
                    _weatherData.postValue(CloudToLocalData.getWeatherData(cityName, cityWeather))
                    localRepository.updateWeather(CloudToLocalData.getWeatherData(cityName,
                        cityWeather))
                    val forecast =
                        cloudRepository.getForecast(response.coord.lat, response.coord.lon)
                    forecast?.let {
                        val day1 = it.list[8]
                        val day2 = it.list[16]
                        val day3 = it.list[24]
                        val skeletonPattern = "ddMM"
                        val workingFormat =
                            DateFormat.getBestDateTimePattern(Locale.getDefault(),
                                skeletonPattern)
                        @SuppressLint("SimpleDateFormat") val monthDayFormat =
                            SimpleDateFormat(workingFormat)

                        _day1Data.postValue(ForecastData(
                            date = monthDayFormat.format(Date(day1.dt * 1000)),
                            iconLink = Repositories.iconsLink[day1.getWeatherIconId()]!!,
                            temperature = day1.getTemp()
                        ))

                        _day2Data.postValue(ForecastData(
                            date = monthDayFormat.format(Date(day2.dt * 1000)),
                            iconLink = Repositories.iconsLink[day2.getWeatherIconId()]!!,
                            temperature = day2.getTemp()
                        ))

                        _day3Data.postValue(ForecastData(
                            date = monthDayFormat.format(Date(day3.dt * 1000)),
                            iconLink = Repositories.iconsLink[day3.getWeatherIconId()]!!,
                            temperature = day3.getTemp()
                        ))
                    }
                }
            } catch (e: NoNetworkConnection) {
                e.printStackTrace()
                _errors.postValue(R.string.error_no_internet)
                getLocalWeather(cityName)
            }
        }
    }

    private suspend fun getLocalWeather(cityName: String) {
        val weatherData = localRepository.getCity(cityName)
        weatherData?.let {
            _weatherData.postValue(it)
        }
    }

    fun getWindDirection(deg: Int): String {
        val direction: Int = (deg / 22.5f).roundToInt()
        return if (deg > 348)
            Repositories.wind[0]
        else
            Repositories.wind[direction]
    }

}