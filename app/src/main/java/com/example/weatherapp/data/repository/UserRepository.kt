package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.entity.UserEntity

interface UserRepository {
    fun getUserId(): Int
    fun setUserId(uid: Int)
    fun clearUserId()
    suspend fun saveUser(user: UserEntity): Boolean
}