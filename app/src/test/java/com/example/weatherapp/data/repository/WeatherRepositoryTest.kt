package com.example.weatherapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.Helper
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.remote.api.ApiService
import com.example.weatherapp.data.remote.dto.WeatherResponse
import com.example.weatherapp.getOrAwaitValue
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class WeatherRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var weatherDao: WeatherDao

    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = WeatherRepositoryImpl(
            apiService = apiService,
            weatherDao = weatherDao,
            dispatcher = mainCoroutineRule.testDispatcher
        )
    }

    @Test
    fun `fetchCurrentWeather with Unauthorized access expected error 401 Unauthorized`() = runTest {

        `when`(
            apiService.getCurrentWeather(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(
            Response.error(401, "Unauthorized".toResponseBody())
        )

        val result =
            weatherRepository.fetchCurrentWeather(lat = "23.3", lon = "22.2", key = "jhdshdjs")

        Assert.assertEquals(true, result is NetworkResult.Error)
    }

    @Test
    fun `fetchCurrentWeather with success response  expected error data null`() = runTest {

        `when`(
            apiService.getCurrentWeather(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(
            Response.success<WeatherResponse>(200, null)
        )

        val result = weatherRepository.fetchCurrentWeather(
            lat = "23.3",
            lon = "22.2",
            key = "jhdshdjs"
        )
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(true, result is NetworkResult.Error)
    }

    @Test
    fun `fetchCurrentWeather with success response  expected weather response`() = runTest {
        val content = Helper.readFileResource("/weather-response.json")
        val jsonObject = Gson().fromJson(content, WeatherResponse::class.java)

        `when`(
            apiService.getCurrentWeather(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(
            Response.success(200, jsonObject)
        )

        val result = weatherRepository.fetchCurrentWeather(
            lat = "23.3",
            lon = "22.2",
            key = "jhdshdjs"
        )
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertNotNull(result.data)
        Assert.assertEquals(298.48, result.data?.temperature)
    }

    @Test
    fun `saveCurrentWeather with expected false`() = runTest {

        val weather = getWeatherEntity()

        `when`(
            weatherDao.insertWeather(
                weather = weather
            )
        ).thenReturn(0)

        val result = weatherRepository.saveCurrentWeather(weather)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(false, result)
    }

    @Test
    fun `saveCurrentWeather with expected true`() = runTest {

        val weather = getWeatherEntity()

        `when`(
            weatherDao.insertWeather(
                weather = weather
            )
        ).thenReturn(1)

        val result = weatherRepository.saveCurrentWeather(weather)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(true, result)
    }

    @Test
    fun `getWeatherHistory with expected emptyList`() = runTest {

        `when`(
            weatherDao.getWeatherHistory(
                anyInt()
            )
        ).thenReturn(MutableLiveData(emptyList()))

        val result = weatherRepository.getWeatherHistory(2).getOrAwaitValue()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(true, result.isEmpty())
    }

    @Test
    fun `getWeatherHistory with expected weatherList`() = runTest {
        val weatherHistoryList = getWeatherHistoryList()
        `when`(
            weatherDao.getWeatherHistory(
                anyInt()
            )
        ).thenReturn(MutableLiveData(weatherHistoryList))

        val result = weatherRepository.getWeatherHistory(2).getOrAwaitValue()
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(3, result.size)
    }

    @Test
    fun `test clearWeatherHistory with expected true`() = runTest {
        val userId = 2

        `when`(
            weatherDao.getHistoryCount(
                userId = userId
            )
        ).thenReturn(0)

        val result = weatherRepository.clearWeatherHistory(userId)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(true, result)
    }

    @Test
    fun `test clearWeatherHistory with expected false`() = runTest {
        val userId = 2

        `when`(
            weatherDao.getHistoryCount(
                userId = userId
            )
        ).thenReturn(2)

        val result = weatherRepository.clearWeatherHistory(userId)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(false, result)
    }

    private fun getWeatherEntity(): WeatherEntity {
        return WeatherEntity(
            uid = 1,
            city = "city",
            country = "country",
            temperature = 23.3,
            timeStamp = 1696407508572,
            weatherIcon = "30d",
        )
    }

    private fun getWeatherHistoryList(): List<WeatherEntity> {
        return listOf(
            WeatherEntity(
                id = 1,
                uid = 2,
                city = "city",
                weatherIcon = "30d",
                timeStamp = 1223232,
                temperature = 22.5,
                country = "country"
            ),
            WeatherEntity(
                id = 2,
                uid = 2,
                city = "city",
                weatherIcon = "30d",
                timeStamp = 23123232,
                temperature = 22.4,
                country = "country"
            ),
            WeatherEntity(
                id = 3,
                uid = 2,
                city = "city",
                weatherIcon = "30d",
                timeStamp = 3232423,
                temperature = 22.3,
                country = "country"
            )
        )
    }

}