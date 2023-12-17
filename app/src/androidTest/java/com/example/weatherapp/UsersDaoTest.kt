package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.database.WeatherDatabase
import com.example.weatherapp.data.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: WeatherDatabase

    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = appDatabase.getUserDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun test_getUserByEmail_expected_user() = runBlocking {
        val data = UserEntity(username = "test user", email = "test@test.com", password = "test123")
        userDao.insert(data)
        val result = userDao.getUserByEmail("test@test.com")
        assertNotNull(result)
        assertEquals(data.email, result?.email)
        assertEquals(data.password, result?.password)
    }

    @Test
    fun test_getUserByEmail_expected_null() = runBlocking {
        val data = UserEntity(username = "test user", email = "test@test.com", password = "test123")
        userDao.insert(data)
        val fetchedUser = userDao.getUserByEmail("test@test2.com")
        assertNull(fetchedUser)
        assertEquals(null, fetchedUser?.email)
        assertEquals(null, fetchedUser?.password)
    }

    @Test
    fun test_insert_user_expected_user() = runBlocking {
        val data = UserEntity(username = "test user", email = "test@test.com", password = "test123")
        userDao.insert(data)
        val fetchUser = userDao.getUserByEmail("test@test.com")
        assertEquals(1, fetchUser!!.id)
    }

    @Test
    fun test_insert_user_expected_null() = runBlocking {
        val data = UserEntity(username = "test user", email = "test@test.com", password = "test123")
        userDao.insert(data)
        val fetchUser = userDao.getUserByEmail("test2@test.com")
        assertNull(fetchUser)
    }

}