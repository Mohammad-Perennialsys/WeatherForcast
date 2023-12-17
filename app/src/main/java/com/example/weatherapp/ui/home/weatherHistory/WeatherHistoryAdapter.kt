package com.example.weatherapp.ui.home.weatherHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.common.utils.loadFromUrl
import com.example.weatherapp.common.utils.timeStampToLocalDateTime
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.databinding.ItemWeatherHistoryBinding

class WeatherHistoryAdapter:
    RecyclerView.Adapter<WeatherHistoryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(binding: ItemWeatherHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val temperature = binding.tvHistoryTemp
        val weatherImage = binding.ivHistoryWeather
        val place = binding.tvHistoryPlace
        val date = binding.tvDateTime
    }

    private val diffUtilCallBack = object :DiffUtil.ItemCallback<WeatherEntity>(){
        override fun areItemsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
           return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemWeatherHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        val view = holder.itemView
        holder.apply {
            temperature.text =view.context.getString(R.string.temp_in_celsius,item.temperature)
            weatherImage.loadFromUrl(view.context.getString(R.string.weather_icon_url,item.weatherIcon))
            place.text = view.context.getString(R.string.location_city_state,item.city,item.country)
            date.text = item.timeStamp.timeStampToLocalDateTime()
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}