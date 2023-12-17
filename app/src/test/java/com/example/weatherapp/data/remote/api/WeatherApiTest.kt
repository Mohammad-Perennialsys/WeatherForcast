package com.example.weatherapp.data.remote.api

import com.example.weatherapp.Helper
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiTest {

    private lateinit var mockWebServer: MockWebServer

    lateinit var weatherApi: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        weatherApi = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    @Test
    fun `test current weather expected weather data`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/weather-response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = weatherApi.getCurrentWeather(lat = "23.3", lon = "22.4", key = "dsdsds")
        mockWebServer.takeRequest()

        Assert.assertNotNull(response.body())
        Assert.assertEquals(true, response.isSuccessful)
        Assert.assertEquals(false, response.body()?.weather?.isEmpty())
    }

    @Test
    fun `test current weather expected error`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("something went wrong")
        mockWebServer.enqueue(mockResponse)

        val response = weatherApi.getCurrentWeather(lat = "23.3", lon = "22.4", key = "dsdsds")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}