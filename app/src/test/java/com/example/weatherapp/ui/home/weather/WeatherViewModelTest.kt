package com.example.weatherapp.ui.home.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.common.utils.NetworkResult
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.repository.UserRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherRepository: WeatherRepository

    @Mock
    lateinit var userRepository: UserRepository

    private lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherViewModel =
            WeatherViewModel(
                weatherRepository = weatherRepository,
                userRepository = userRepository,
            )
    }

    @Test
    fun `fetchCurrentWeather expected NetworkState Error`() = runTest {
        Mockito.`when`(
            weatherRepository.fetchCurrentWeather(
                lat = Mockito.anyString(),
                lon = Mockito.anyString(),
                key = Mockito.anyString()
            )
        ).thenReturn(
            NetworkResult.Error(message = "Some Error Occurred.")
        )
        weatherViewModel.setLocation(lat = 20.3, lon = 20.4)
        weatherViewModel.fetchCurrentWeather()
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val result = weatherViewModel.weatherUiState.getOrAwaitValue()

        assertEquals(true, result is UiState.Error)
    }

    @Test
    fun `fetchCurrentWeather expected NetworkState Loading`() = runTest {

        Mockito.`when`(
            weatherRepository.fetchCurrentWeather(
                lat = Mockito.anyString(),
                lon = Mockito.anyString(),
                key = Mockito.anyString()
            )
        ).thenReturn(NetworkResult.Loading())

        weatherViewModel.setLocation(lat = 20.3, lon = 20.4)
        weatherViewModel.fetchCurrentWeather()
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val result = weatherViewModel.weatherUiState.getOrAwaitValue()
        assertEquals(true, result is UiState.Loading)
    }

    @Test
    fun `fetchCurrentWeather expected NetworkState Success`() = runTest {
        val userId = 2
        val timestamp = 12233443L
        val weatherData = getWeatherInfo()
        val weatherEntity = getWeatherEntity(userId = userId, timestamp = timestamp)
        Mockito.`when`(weatherRepository.getTimeMillis()).thenReturn(timestamp)
        Mockito.`when`(userRepository.getUserId()).thenReturn(userId)
        Mockito.`when`(
            weatherRepository.fetchCurrentWeather(
                lat = Mockito.anyString(),
                lon = Mockito.anyString(),
                key = Mockito.anyString()
            )
        ).thenReturn(
            NetworkResult.Success(data = weatherData)
        )
        Mockito.`when`(weatherRepository.saveCurrentWeather(weatherEntity)).thenReturn(true)

        weatherViewModel.setLocation(lat = 20.3, lon = 20.4)
        weatherViewModel.fetchCurrentWeather()

        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val result = weatherViewModel.weatherUiState.getOrAwaitValue()

        assertEquals(true, result is UiState.Success)
    }

    @Test
    fun `test setLocation with lat, lon expected true`() {
        val testData = Pair(20.4, 22.3)
        weatherViewModel.setLocation(lat = testData.first, lon = testData.second)
        val result = weatherViewModel.currentLocation
        assertNotNull(result)
    }

    private fun getWeatherEntity(userId: Int, timestamp: Long): WeatherEntity {
        return WeatherEntity(
            uid = userId,
            city = "city",
            country = "country",
            temperature = 26.4,
            weatherIcon = "30d",
            timeStamp = timestamp
        )
    }

    private fun getWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            city = "city",
            country = "country",
            temperature = 26.4,
            description = "description",
            weatherIcon = "30d",
            sunset = 232424424,
            sunrise = 232428787,
            timeZoneId = 232344,
        )
    }
}