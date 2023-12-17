package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.model.WeatherInfo


interface WeatherRepository {
    suspend fun fetchCurrentWeather(
        lat: String,
        lon: String,
        key: String
    ): NetworkResult<WeatherInfo>

    suspend fun saveCurrentWeather(weather: WeatherEntity): Boolean
    fun getWeatherHistory(userId: Int): LiveData<List<WeatherEntity>>
    suspend fun clearWeatherHistory(userId: Int): Boolean
    fun getTimeMillis(): Long
}