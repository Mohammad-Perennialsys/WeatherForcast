package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.dao.WeatherDao
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
        val builder = Room.databaseBuilder(
            context = context,
            WeatherDatabase::class.java,
            "weather_db"
        )
        val factory =
            SupportFactory(SQLiteDatabase.getBytes(BuildConfig.DB_PASS_PHRASE.toCharArray()))
        builder.openHelperFactory(factory)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: WeatherDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.getWeatherDao()
    }
}