package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.dto.WeatherResponse

fun WeatherResponse.toDomainWeatherDetails(): WeatherInfo {
    return WeatherInfo(
        city = this.name,
        country = this.sys.country,
        temperature = this.main.temp,
        description = this.weather[0].main,
        sunrise = this.sys.sunrise,
        timeZoneId = this.timezone,
        sunset = this.sys.sunset,
        weatherIcon = this.weather[0].icon,
    )
}