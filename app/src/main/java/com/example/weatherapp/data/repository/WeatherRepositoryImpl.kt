package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.mapper.toDomainWeatherDetails
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.api.ApiService
import com.example.weatherapp.data.remote.dto.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
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

    override suspend fun saveCurrentWeather(weather: WeatherEntity): Boolean {
        return withContext(dispatcher) {
            val weatherId = weatherDao.insertWeather(weather)
            weatherId > 0
        }
    }

    override fun getWeatherHistory(userId: Int): LiveData<List<WeatherEntity>> {
        return weatherDao.getWeatherHistory(userId)
    }

    override suspend fun clearWeatherHistory(userId: Int): Boolean {
        return withContext(dispatcher) {
            weatherDao.clearWeather(userId)
            val weatherCount = weatherDao.getHistoryCount(userId)
            weatherCount == 0
        }
    }

    override fun getTimeMillis(): Long = System.currentTimeMillis()
}