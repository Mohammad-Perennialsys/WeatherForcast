package com.example.weatherapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var uid:Int? = null,
    val city: String? = null,
    val country: String? = null,
    val temperature: Double? = null,
    @ColumnInfo(name = "w_icon")
    val weatherIcon: String? = null,
    @ColumnInfo(name = "timestamp")
    var timeStamp:Long = 0
)
