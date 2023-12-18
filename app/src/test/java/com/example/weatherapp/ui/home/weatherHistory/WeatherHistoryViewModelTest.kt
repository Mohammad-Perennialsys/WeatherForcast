package com.example.weatherapp.ui.home.weatherHistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherHistoryViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherRepository: WeatherRepositoryImpl

    @Mock
    lateinit var userRepository: UserRepositoryImpl

    private lateinit var weatherHistoryViewModel: WeatherHistoryViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherHistoryViewModel = WeatherHistoryViewModel(
            weatherRepository = weatherRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `clearWeatherHistory expected true`() = runTest {
        Mockito.`when`(weatherRepository.clearWeatherHistory(Mockito.anyInt())).thenReturn(true)

        weatherHistoryViewModel.clearWeatherHistory()
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val result = weatherHistoryViewModel.isHistoryCleared.getOrAwaitValue()
        assertEquals(true, result)

    }

    @Test
    fun `clearWeatherHistory expected false`() = runTest {
        Mockito.`when`(weatherRepository.clearWeatherHistory(Mockito.anyInt())).thenReturn(false)

        weatherHistoryViewModel.clearWeatherHistory()
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val result = weatherHistoryViewModel.isHistoryCleared.getOrAwaitValue()
        assertEquals(false, result)
    }

    @Test
    fun `setClearHistoryToastShown with false expected false`() {
        weatherHistoryViewModel.setClearHistoryToastShown(false)

        val result = weatherHistoryViewModel.isClearHistoryToastShown

        assertEquals(false, result)
    }

    @Test
    fun `setClearHistoryToastShown with true expected true`() {
        weatherHistoryViewModel.setClearHistoryToastShown(true)

        val result = weatherHistoryViewModel.isClearHistoryToastShown

        assertEquals(true, result)
    }
}