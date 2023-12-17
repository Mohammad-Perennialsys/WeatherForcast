package com.example.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.local.entity.UserEntity
import com.example.weatherapp.data.local.entity.WeatherEntity

@Database(entities = [UserEntity::class, WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getWeatherDao(): WeatherDao

}