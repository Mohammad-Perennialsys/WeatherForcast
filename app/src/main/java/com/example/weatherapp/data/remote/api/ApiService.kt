package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        key: String,
        @Query("units")
        units: String? = "metric",
    ): Response<WeatherResponse>
}