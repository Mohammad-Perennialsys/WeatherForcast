package com.example.weatherapp.data.repository

import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.model.WeatherInfo


interface WeatherRepository {
    suspend fun fetchCurrentWeather(
        lat: String,
        lon: String,
        key: String
    ): NetworkResult<WeatherInfo>
}