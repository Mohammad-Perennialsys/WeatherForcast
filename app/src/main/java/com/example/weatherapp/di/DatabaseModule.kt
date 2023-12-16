package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context = context,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: WeatherDatabase): UserDao {
        return database.getUserDao()
    }
}