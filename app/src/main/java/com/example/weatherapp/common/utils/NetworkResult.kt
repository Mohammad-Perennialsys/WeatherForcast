package com.example.weatherapp.common.utils

sealed class NetworkResult<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(data: T? = null, message: String?) :
        NetworkResult<T>(data = data, error = message)
    class Loading<T> : NetworkResult<T>()
}