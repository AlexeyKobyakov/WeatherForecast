package com.alexeykov.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alexeykov.weather.R
import com.alexeykov.weather.databinding.FragmentDetailsBinding
import com.alexeykov.weather.viewmodels.DetailsViewModel
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentDetailsBinding is null")

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.calculateData(
            iconsArray = resources.getStringArray(R.array.icons_map).toCollection(ArrayList()),
            windsArray = resources.getStringArray(R.array.winds).toCollection(ArrayList()),
            weatherKeys = resources.getStringArray(R.array.weather_keys).toCollection(ArrayList()),
            weatherValues = resources.getStringArray(R.array.weather_values)
                .toCollection(ArrayList())
        )

        arguments?.getString("city", "")?.let {
            (activity as AppCompatActivity).supportActionBar?.title = it
            detailsViewModel.getWeather(it)
        }

        detailsViewModel.day1Data.observe(requireActivity()) {
            with(binding) {
                date1.text = it.date
                Glide.with(skyDay1)
                    .load(it.iconLink)
                    .into(skyDay1)
                temperatureDay1.text = it.temperature
            }
        }

        detailsViewModel.day2Data.observe(requireActivity()) {
            with(binding) {
                date2.text = it.date
                Glide.with(skyDay2)
                    .load(it.iconLink)
                    .into(skyDay2)
                temperatureDay2.text = it.temperature
            }
        }

        detailsViewModel.day3Data.observe(requireActivity()) {
            with(binding) {
                date3.text = it.date
                Glide.with(skyDay3)
                    .load(it.iconLink)
                    .into(skyDay3)
                temperatureDay3.text = it.temperature
            }
        }

        detailsViewModel.weatherData.observe(requireActivity()) { weatherData ->
            with(binding) {
                Glide.with(sky)
                    .load(weatherData.iconLink)
                    .into(sky)
                temperature.text = weatherData.temperature
                temperatureFeels.text = weatherData.temp_feel
                skyText.text = weatherData.weather
                val lineSignText = "${weatherData.visibility} ${getString(R.string.unit_distance)}"
                lineSign.text = lineSignText
                val pressureText = "${weatherData.pressure} ${getString(R.string.unit_pressure)}"
                pressure.text = pressureText
                humidity.text = weatherData.humidity
                val windText = "${weatherData.windSpeed} ${getString(R.string.unit_speed)} " +
                        "(${detailsViewModel.getWindDirection(weatherData.windDeg)})"
                wind.text = windText
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}