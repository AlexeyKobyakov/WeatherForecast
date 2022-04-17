package com.alexeykov.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alexeykov.weather.R
import com.alexeykov.weather.Repositories
import com.alexeykov.weather.databinding.FragmentDetailsBinding
import com.alexeykov.weather.viewmodels.DetailsViewModel
import com.alexeykov.weather.viewmodels.viewModelCreator
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentDetailsBinding is null")

    private val detailsViewModel: DetailsViewModel by viewModelCreator {
        DetailsViewModel(
            Repositories.localRepository,
            Repositories.cloudRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("city", "")?.let {
            (activity as AppCompatActivity).supportActionBar?.title = it
            detailsViewModel.getWeather(it)
        }

        observeWeather()
    }

    private fun observeWeather() {
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
                temperatureFeels.text = weatherData.tempFeel
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

        detailsViewModel.errors.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            binding.threeDaysLayout.visibility = View.GONE
            binding.threeDaysTextView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}