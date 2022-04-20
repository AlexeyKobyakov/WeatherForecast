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
    private var lastFavoritePosition: Int? = null
    private var lastFavoriteId: Int? = null

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
                lastFavoritePosition = holder.adapterPosition
                lastFavoriteId = item.id
            }
            if (position == 0 && item.isFavorite != 0) {
                textFavorites.visibility = View.VISIBLE
                textOtherCities.visibility = View.GONE
            } else
                textFavorites.visibility = View.GONE

            if (position > 0)
                if (item.isFavorite == 0 && items[position - 1].isFavorite != 0) {
                    textOtherCities.visibility = View.VISIBLE
                    textFavorites.visibility = View.GONE
                } else
                    textOtherCities.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    fun renderItems(items: List<WeatherShortData>) {
        val oldItems = this.items
        val diffUtilCallBack = WeatherShortDataDiffUtil(oldItems, items)
        val result = DiffUtil.calculateDiff(diffUtilCallBack)
        this.items = items
        result.dispatchUpdatesTo(this)

        //Need to add smarter logic
        lastFavoritePosition?.let {
            calcUpdates(it, lastFavoriteId!!, items)
            lastFavoritePosition = null
        }
    }

    private fun calcUpdates(
        lastFavoritePosition: Int,
        lastFavoriteId: Int,
        items: List<WeatherShortData>,
    ) {
        val updateList = HashSet<Int>()
        if (lastFavoritePosition == 0 && items.size > 1) {
            notifyItemChanged(1)
            updateList.add(1)
        } else if (lastFavoritePosition > 0 && items.size - 1 > lastFavoritePosition) {
            notifyItemChanged(lastFavoritePosition - 1)
            notifyItemChanged(lastFavoritePosition + 1)
            updateList.add(lastFavoritePosition - 1)
            updateList.add(lastFavoritePosition + 1)
        } else if (lastFavoritePosition == items.size - 1 && items.size > 1) {
            notifyItemChanged(lastFavoritePosition - 1)
            updateList.add(lastFavoritePosition - 1)
        }

        var newFavoritePosition = 0
        items.indices.forEach {
            if (items[it].id == lastFavoriteId)
                newFavoritePosition = it
        }

        if (newFavoritePosition == 0 && items.size > 1) {
            if (!updateList.contains(1))
                notifyItemChanged(1)
        } else if (newFavoritePosition > 0 && items.size - 1 > newFavoritePosition) {
            if (!updateList.contains(newFavoritePosition - 1))
                notifyItemChanged(newFavoritePosition - 1)
            if (!updateList.contains(newFavoritePosition + 1))
                notifyItemChanged(newFavoritePosition + 1)
        } else if (newFavoritePosition == items.size - 1 && items.size > 1)
            if (!updateList.contains(newFavoritePosition - 1))
                notifyItemChanged(newFavoritePosition - 1)
        notifyItemChanged(0)
    }

    class Holder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)
}