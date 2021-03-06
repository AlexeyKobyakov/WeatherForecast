package com.alexeykov.weather.viewmodels

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexeykov.weather.R
import com.alexeykov.weather.model.data.CloudToLocalData
import com.alexeykov.weather.ResourcesData
import com.alexeykov.weather.model.NoNetworkConnection
import com.alexeykov.weather.model.StorageException
import com.alexeykov.weather.model.data.WeatherData
import com.alexeykov.weather.model.data.ForecastData
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.room.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@HiltViewModel
class DetailsViewModel @Inject constructor(
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

    private var wind: ArrayList<String> = ArrayList()

    fun getWeather(cityName: String) {
        job.launch {
            try {
                val city = localRepository.getCity(cityName)
                val response = cloudRepository.getWeatherInCity(cityName)

                response?.let { cityWeather ->
                    val weather = CloudToLocalData.getWeatherData(
                        cityId = city?.id ?: 0,
                        cityName = cityName,
                        isFavorite = city?.isFavorite ?: 0,
                        cityWeather = cityWeather)

                    _weatherData.postValue(weather)

                    localRepository.updateWeather(weather)
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
                            iconLink = ResourcesData.iconsLink[day1.getWeatherIconId()]!!,
                            temperature = day1.getTemp()
                        ))

                        _day2Data.postValue(ForecastData(
                            date = monthDayFormat.format(Date(day2.dt * 1000)),
                            iconLink = ResourcesData.iconsLink[day2.getWeatherIconId()]!!,
                            temperature = day2.getTemp()
                        ))

                        _day3Data.postValue(ForecastData(
                            date = monthDayFormat.format(Date(day3.dt * 1000)),
                            iconLink = ResourcesData.iconsLink[day3.getWeatherIconId()]!!,
                            temperature = day3.getTemp()
                        ))
                    }
                }
            } catch (e: NoNetworkConnection) {
                e.printStackTrace()
                _errors.postValue(R.string.error_no_internet)

                getLocalWeather(cityName)

            } catch (e: StorageException) {
                e.printStackTrace()
                _errors.postValue(R.string.error_load_data)
            }
        }
    }

    private suspend fun getLocalWeather(cityName: String) {
        val weatherData = localRepository.getCity(cityName)
        weatherData?.let {
            _weatherData.postValue(it)
        }
    }

    fun setWind(array: ArrayList<String>) {
        wind = array
    }

    fun getWindDirection(deg: Int): String {
        val direction: Int = (deg / 22.5f).roundToInt()
        return if (deg > 348)
            wind[0]
        else
            wind[direction]
    }

}