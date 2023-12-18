package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.local.database.WeatherDatabase
import com.example.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.runBlocking
import org.junit.*

class WeatherDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        weatherDao = appDatabase.getWeatherDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun test_insert_weather_expected_success() = runBlocking {
        val weather1 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.4,
            weatherIcon = "30d",
            timeStamp = 12232334
        )
        weatherDao.insertWeather(weather1)

        val result = weatherDao.getWeatherHistory(userId = 1).getOrAwaitValue()
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun test_get_weather_history_expected_weather() = runBlocking {
        val weather1 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.4,
            weatherIcon = "30d",
            timeStamp = 12232334
        )
        val weather2 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.2,
            weatherIcon = "30d",
            timeStamp = 12232335
        )
        weatherDao.insertWeather(weather1)
        weatherDao.insertWeather(weather2)

        val result = weatherDao.getWeatherHistory(userId = 1).getOrAwaitValue()
        Assert.assertEquals(2, result.size)
    }

    @Test
    fun test_get_weather_history_expected_no_result() = runBlocking {
        val weather1 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.4,
            weatherIcon = "30d",
            timeStamp = 12232334
        )
        val weather2 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.2,
            weatherIcon = "30d",
            timeStamp = 12232335
        )
        weatherDao.insertWeather(weather1)
        weatherDao.insertWeather(weather2)
        val result = weatherDao.getWeatherHistory(userId = 2).getOrAwaitValue()
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun test_clearWeather_expected_success() = runBlocking {
        val weather1 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.4,
            weatherIcon = "30d",
            timeStamp = 12232334
        )
        val weather2 = WeatherEntity(
            uid = 1,
            city = "mumbai",
            country = "India",
            temperature = 23.2,
            weatherIcon = "30d",
            timeStamp = 12232335
        )
        weatherDao.insertWeather(weather1)
        weatherDao.insertWeather(weather2)
        weatherDao.clearWeather(1)
        val result = weatherDao.getWeatherHistory(userId = 1).getOrAwaitValue()
        Assert.assertEquals(0, result.size)

    }

}