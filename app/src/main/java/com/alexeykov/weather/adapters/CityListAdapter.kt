package com.alexeykov.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexeykov.weather.R
import com.alexeykov.weather.databinding.ItemCityBinding
import com.alexeykov.weather.model.data.WeatherShortData
import com.bumptech.glide.Glide

class CityListAdapter(
    private val listener: Listener,
) : RecyclerView.Adapter<CityListAdapter.Holder>(), View.OnClickListener {

    private var items: List<WeatherShortData> = emptyList()

    interface Listener {
        fun onItemClicked(cityName: String)
        fun onDeleteClicked(cityName: String)
        fun onFavoriteClicked(item: WeatherShortData)
    }

    override fun onClick(v: View) {
        val item = v.tag as WeatherShortData
        listener.onItemClicked(item.cityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.itemView.tag = item
        with(holder.binding) {
            cityName.text = item.cityName
            temperature.text = item.temperature
            Glide.with(weatherIcon)
                .load(item.iconLink)
                .into(weatherIcon)
            if (item.isFavorite == 0)
                Glide.with(iconFavorite)
                    .load(R.drawable.ic_favorite_not)
                    .into(iconFavorite)
            else
                Glide.with(iconFavorite)
                    .load(R.drawable.ic_favorite)
                    .into(iconFavorite)

            iconDelete.setOnClickListener {
                listener.onDeleteClicked(item.cityName)
            }

            iconFavorite.setOnClickListener {
                listener.onFavoriteClicked(item)
            }
/*            if (position == 0 && item.isFavorite != 0)
                textFavorites.visibility = View.VISIBLE
            else
                textFavorites.visibility = View.GONE

            if (position > 0)
                if (item.isFavorite == 0 && items[position - 1].isFavorite != 0)
                    textOtherCities.visibility = View.VISIBLE
                else
                    textOtherCities.visibility = View.GONE*/
        }
    }

    override fun getItemCount(): Int = items.size

    fun renderItems(items: List<WeatherShortData>) {
        val diffUtilCallBack = WeatherShortDataDiffUtil(this.items, items)
        val result = DiffUtil.calculateDiff(diffUtilCallBack)
        this.items = items
        result.dispatchUpdatesTo(this)
    }

    class Holder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)
}