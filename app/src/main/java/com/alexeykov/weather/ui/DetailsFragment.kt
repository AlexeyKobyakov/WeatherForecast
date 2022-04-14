package com.alexeykov.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alexeykov.weather.R
import com.alexeykov.weather.databinding.FragmentDetailsBinding
import com.alexeykov.weather.repository.cloud.ApiFactory
import com.alexeykov.weather.repository.cloud.WeatherRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentDetailsBinding is null")

    private val repository: WeatherRepository = WeatherRepository(ApiFactory.weatherApi)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val city: String = arguments?.getString("city", "")!!
        city.let { thisCity ->
            (activity as AppCompatActivity).supportActionBar?.title = thisCity
            CoroutineScope(Dispatchers.IO).launch {
                val response = repository.getWeatherInCity("Москва")
                response?.let {
                    val iconsLink = getIconsMap()
                    val forecast = repository.getForecast(response.coord.lat, response.coord.lon)
                    forecast?.let {
                        val day1 = it.list[8]
                        val day2 = it.list[16]
                        val day3 = it.list[24]
                        CoroutineScope(Dispatchers.Main).launch {
                            with(binding) {
                                // Берем формат только даты и месяца в зависимости от локализации
                                val skeletonPattern = "ddMM"
                                val workingFormat =
                                    DateFormat.getBestDateTimePattern(Locale.getDefault(),
                                        skeletonPattern)
                                @SuppressLint("SimpleDateFormat") val monthDayFormat =
                                    SimpleDateFormat(workingFormat)
                                date1.text = monthDayFormat.format(Date(day1.dt * 1000))
                                Glide.with(skyDay1)
                                    .load(iconsLink[day1.getWeatherIconId()])
                                    .into(skyDay1)
                                temperatureDay1.text = day1.getTemp()

                                date2.text = monthDayFormat.format(Date(day2.dt * 1000))
                                Glide.with(skyDay2)
                                    .load(iconsLink[day2.getWeatherIconId()])
                                    .into(skyDay2)
                                temperatureDay2.text = day2.getTemp()

                                date3.text = monthDayFormat.format(Date(day3.dt * 1000))
                                Glide.with(skyDay3)
                                    .load(iconsLink[day3.getWeatherIconId()])
                                    .into(skyDay3)
                                temperatureDay3.text = day3.getTemp()
                            }
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        with(binding) {
                            val url = iconsLink[it.getWeatherIconId()]
                            Glide.with(sky)
                                .load(url)
                                .into(sky)
                            temperature.text = it.getTemp()
                            temperatureFeels.text = it.getTempFeels()
                            humidity.text = it.getHumidity()
                            val pressureText =
                                "${it.main.pressure} ${getString(R.string.unit_pressure)}"
                            pressure.text = pressureText
                            val windText = "${it.wind.speed} ${getString(R.string.unit_speed)} (${
                                getWindDirection(it.wind.deg)
                            })"
                            wind.text = windText
                            skyText.text = getWeatherMap()[it.getWeatherId()]
                            val visibility = "${it.getSign()} ${getString(R.string.unit_distance)}"
                            lineSign.text = visibility
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getIconsMap(): LinkedHashMap<String, String> {
        val array: Array<out String> = resources.getStringArray(R.array.icons_map)
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(array.size)
        array.forEach {
            val str: List<String> = it.split("|")
            linkedHashMap[str[0]] = str[1]
        }
        return linkedHashMap
    }

    private fun getWeatherMap(): LinkedHashMap<String, String> {
        val arrayKeys: Array<out String> = resources.getStringArray(R.array.weather_keys)
        val arrayValues: Array<out String> = resources.getStringArray(R.array.weather_values)
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(arrayKeys.size)
        arrayKeys.indices.forEach {
            linkedHashMap[arrayKeys[it]] = arrayValues[it]
        }
        return linkedHashMap
    }

    private fun getWindDirection(deg: Int): String {
        val wind = resources.getStringArray(R.array.winds)
        val direction: Int = (deg / 22.5f).roundToInt()
        return if (deg > 348)
            wind[0]
        else
            wind[direction]

    }
}