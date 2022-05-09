package com.alexeykov.weather

import android.content.Context
import java.util.LinkedHashMap

object ResourcesData {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    val weather: LinkedHashMap<String, String> by lazy {
        getWeatherMap()
    }

    val iconsLink: LinkedHashMap<String, String> by lazy {
        getIconsMap()
    }

    private fun getIconsMap(): LinkedHashMap<String, String> {
        val iconsArray = appContext.resources.getStringArray(R.array.icons_array)
        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(iconsArray.size - 1)

        iconsArray.forEach {
            val str: List<String> = it.split("|")
            linkedHashMap[str[0]] = str[1]
        }

        return linkedHashMap
    }

    private fun getWeatherMap(): LinkedHashMap<String, String> {
        val arrayKeys = appContext.resources.getStringArray(R.array.weather_keys)
        val arrayValues = appContext.resources.getStringArray(R.array.weather_values)

        val linkedHashMap: LinkedHashMap<String, String> = LinkedHashMap(arrayKeys.size)

        for (i in arrayKeys.indices) {
            linkedHashMap[arrayKeys[i]] = arrayValues[i]
        }

        return linkedHashMap
    }
}