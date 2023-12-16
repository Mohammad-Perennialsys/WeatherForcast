package com.example.weatherapp.ui.home.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private var _currentLocation:Pair<Double,Double>? = null
    val currentLocation:Pair<Double,Double>? get() = _currentLocation
    fun setLocation(lat: Double, lon: Double) {
        _currentLocation = Pair(lat, lon)
    }

    private var _currentWeather: WeatherInfo? = null
    val currentWeather:WeatherInfo? get() = _currentWeather

    private var isWeatherSaved: Boolean = false

    private val _weatherUiState: MutableLiveData<UiState<WeatherInfo?>> = MutableLiveData()
    val weatherUiState: LiveData<UiState<WeatherInfo?>> get() = _weatherUiState

    fun fetchCurrentWeather() {
        if (!isWeatherSaved && currentLocation != null) {
            val lat = currentLocation!!.first.toString()
            val lon = currentLocation!!.second.toString()
            val key = BuildConfig.API_KEY
            viewModelScope.launch {
                val result = weatherRepository.fetchCurrentWeather(lat = lat, lon = lon, key = key)
                when (result) {
                    is NetworkResult.Loading -> {
                        _weatherUiState.postValue(UiState.Loading)
                    }
                    is NetworkResult.Success -> {
                        val weather = result.data
                        _weatherUiState.postValue(UiState.Success(data = weather))
                    }
                    is NetworkResult.Error -> {
                        _weatherUiState.postValue(
                            UiState.Error(message = result.error ?: "Error to Load Data")
                        )
                    }
                }
            }
        }
    }
}