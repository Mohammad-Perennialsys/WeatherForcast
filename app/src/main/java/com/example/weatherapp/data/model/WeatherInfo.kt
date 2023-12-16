package com.example.weatherapp.data.model

data class WeatherInfo(
    val city: String = "",
    val country: String = "",
    val temperature: Double = 0.0,
    val description: String = "",
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val timeZoneId:Int? = 0,
    val weatherIcon: String = ""
)
