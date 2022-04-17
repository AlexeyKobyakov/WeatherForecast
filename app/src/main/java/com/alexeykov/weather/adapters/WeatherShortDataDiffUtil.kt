package com.alexeykov.weather.adapters

import androidx.recyclerview.widget.DiffUtil
import com.alexeykov.weather.model.data.WeatherShortData

class WeatherShortDataDiffUtil(
    private val oldList: List<WeatherShortData>,
    private val newList: List<WeatherShortData>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
