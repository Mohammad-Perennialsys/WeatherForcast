package com.example.weatherapp.data.repository

import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.mapper.toDomainWeatherDetails
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.api.ApiService
import com.example.weatherapp.data.remote.dto.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher,
) : WeatherRepository {
    override suspend fun fetchCurrentWeather(
        lat: String,
        lon: String,
        key: String
    ): NetworkResult<WeatherInfo> {
        return withContext(dispatcher) {
            try {
                NetworkResult.Loading<WeatherInfo>()
                val response = apiService.getCurrentWeather(lat = lat, lon = lon, key = key)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        NetworkResult.Success(data = responseBody.toDomainWeatherDetails())
                    } else {
                        NetworkResult.Error(message = "Data Not Found")
                    }
                } else {
                    if (response.errorBody() != null) {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        NetworkResult.Error(message = errorResponse.message)
                    } else {
                        NetworkResult.Error(message = "Some Api Error")
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error(message = e.localizedMessage ?: "Network Error")
            }
        }
    }
}