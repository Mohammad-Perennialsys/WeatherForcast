package com.example.weatherapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity):Long

    @Query("SELECT * FROM weather WHERE uid = :userId ORDER BY id DESC")
    fun getWeatherHistory(userId: Int): LiveData<List<WeatherEntity>>

    @Query("SELECT COUNT(id) FROM weather WHERE uid = :userId")
    fun getHistoryCount(userId: Int):Int

    @Query("DELETE FROM weather WHERE uid = :userId")
    suspend fun clearWeather(userId: Int)
}