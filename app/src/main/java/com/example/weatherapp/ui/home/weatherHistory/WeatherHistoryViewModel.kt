package com.example.weatherapp.ui.home.weatherHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.repository.UserRepository
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherHistoryViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    userRepository: UserRepository,
) : ViewModel() {

    private val userId: Int = userRepository.getUserId()
    private val _isHistoryCleared: MutableLiveData<Boolean> = MutableLiveData()
    val isHistoryCleared: LiveData<Boolean> get() = _isHistoryCleared
    private var _isClearHistoryToastShown = false
    val isClearHistoryToastShown get() = _isClearHistoryToastShown
    fun setClearHistoryToastShown(value: Boolean) {
        _isClearHistoryToastShown = value
    }

    val weatherHistory: LiveData<List<WeatherEntity>>
        get() =
            weatherRepository.getWeatherHistory(userId = userId)

    fun clearWeatherHistory() {
        viewModelScope.launch {
            val result = weatherRepository.clearWeatherHistory(userId = userId)
            _isHistoryCleared.value = result
        }
    }
}