package com.alexeykov.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.alexeykov.weather.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentDetailsBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val city = arguments?.getString("city")
        city.let {
            (activity as AppCompatActivity).supportActionBar?.title = it
        }
        val wind = resources.getStringArray(R.array.winds)
        val iconsMap = getIconsMap()
        val weatherMap = getWeatherMap()
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
}