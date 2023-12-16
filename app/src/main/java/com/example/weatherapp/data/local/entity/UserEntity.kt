package com.example.weatherapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String
)
