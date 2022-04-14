package com.alexeykov.weather.viewmodels

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexeykov.weather.repository.WeatherData
import com.alexeykov.weather.repository.ForecastData
import com.alexeykov.weather.repository.cloud.ApiFactory
import com.alexeykov.weather.repository.cloud.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class DetailsViewModel : ViewModel() {

    private var iconsLink: LinkedHashMap<String, String> = LinkedHashMap()
    private var weather: LinkedHashMap<String, String> = LinkedHashMap()
    private var wind: ArrayList<String> = ArrayList()

    private var _weatherData: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var weatherData: LiveData<WeatherData> = _weatherData

    private var _day1Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day1Data: LiveData<ForecastData> = _day1Data

    private var _day2Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day2Data: LiveData<ForecastData> = _day2Data

    private var _day3Data: MutableLiveData<ForecastData> = MutableLiveData<ForecastData>()
    var day3Data: LiveData<ForecastData> = _day3Data

    private val job: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val repository: WeatherRepository = WeatherRepository(ApiFactory.weatherApi)

    fun getWeather(city: String) {
        job.launch {
            val response = repository.getWeatherInCity(city)
            response?.let { cityWeather ->
                _weatherData.postValue(WeatherData(
                    id = cityWeather.id,
                    cityName = city,
                    coordLat = cityWeather.coord.lat,
                    coordLon = cityWeather.coord.lon,
                    temperature = cityWeather.getTemp(),
                    temp_feel = cityWeather.getTempFeels(),
                    pressure = cityWeather.main.pressure,
                    humidity = cityWeather.getHumidity(),
                    visibility = cityWeather.getSign(),
                    weather = weather[cityWeather.getWeatherId()]!!,
                    iconLink = iconsLink[cityWeather.getWeatherIconId()]!!,
                    windSpeed = cityWeather.wind.speed,
                    windDeg = cityWeather.wind.deg
                ))

                val forecast = repository.getForecast(response.coord.lat, response.coord.lon)
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
                        iconLink = iconsLink[day1.getWeatherIconId()]!!,
                        temperature = day1.getTemp()
                    ))

                    _day2Data.postValue(ForecastData(
                        date = monthDayFormat.format(Date(day2.dt * 1000)),
                        iconLink = iconsLink[day2.getWeatherIconId()]!!,
                        temperature = day2.getTemp()
                    ))

                    _day3Data.postValue(ForecastData(
                        date = monthDayFormat.format(Date(day3.dt * 1000)),
                        iconLink = iconsLink[day3.getWeatherIconId()]!!,
                        temperature = day3.getTemp()
                    ))
                }
            }
        }
    }

    private fun getIconsMap(iconsArray: ArrayList<String>): LinkedHashMap<String, String> {
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(iconsArray.size - 1)
        iconsArray.forEach {
            val str: List<String> = it.split("|")
            linkedHashMap[str[0]] = str[1]
        }
        return linkedHashMap
    }

    private fun getWeatherMap(
        arrayKeys: ArrayList<String>,
        arrayValues: ArrayList<String>,
    ): LinkedHashMap<String, String> {
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(arrayKeys.size)
        arrayKeys.indices.forEach {
            linkedHashMap[arrayKeys[it]] = arrayValues[it]
        }
        return linkedHashMap
    }

    fun getWindDirection(deg: Int): String {
        val direction: Int = (deg / 22.5f).roundToInt()
        return if (deg > 348)
            wind[0]
        else
            wind[direction]
    }

    fun calculateData(
        iconsArray: ArrayList<String>,
        windsArray: ArrayList<String>,
        weatherKeys: ArrayList<String>,
        weatherValues: ArrayList<String>,
    ) {
        iconsLink = getIconsMap(iconsArray)
        wind = windsArray
        weather = getWeatherMap(weatherKeys, weatherValues)
    }
}