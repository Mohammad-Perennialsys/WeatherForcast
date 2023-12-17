package com.example.weatherapp.data.repository

import android.content.SharedPreferences
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.common.utils.Constants
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.entity.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserRepositoryImplTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var userDao: UserDao

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var editor: SharedPreferences.Editor

    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        userRepository = UserRepositoryImpl(
            dispatcher = mainCoroutineRule.testDispatcher,
            userDao = userDao,
            sharedPreference = sharedPreferences
        )
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)

    }

    @Test
    fun `getUserByEmail with registered email expected user`() = runTest {
        val testUser =
            UserEntity(username = "test", email = "test@test.com", password = "Passt@123")
        Mockito.`when`(userDao.getUserByEmail(Mockito.anyString())).thenReturn(testUser)

        val result = userRepository.getUserByEmail("test@test.com")
        assertNotNull(result)
        assertEquals("test", result?.username)
        assertEquals("test@test.com", result?.email)
    }

    @Test
    fun `getUserByEmail with unregistered user expected null`() = runTest {
        Mockito.`when`(userDao.getUserByEmail(Mockito.anyString())).thenReturn(null)
        val result = userRepository.getUserByEmail("test@test.com")
        assertNull(result)
        assertEquals(null, result?.username)
    }

    @Test
    fun `saveUser with success insertion expected true`() = runTest {

        val testUser =
            UserEntity(username = "Test3", email = "test@test.com", password = "Pass@123")
        Mockito.`when`(userDao.insert(testUser)).thenReturn(2)

        val user = UserEntity(username = "Test3", email = "test@test.com", password = "Pass@123")
        val result = userRepository.saveUser(user)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Mockito.verify(userDao).insert(testUser)
        assertEquals(true, result)

    }

    @Test
    fun `saveUser with failure insertion expected false`() = runTest {

        val testUser =
            UserEntity(username = "Test3", email = "test@test.com", password = "Pass@123")
        Mockito.`when`(userDao.insert(testUser)).thenReturn(0)

        val user = UserEntity(username = "Test3", email = "test@test.com", password = "Pass@123")
        val result = userRepository.saveUser(user)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        Mockito.verify(userDao).insert(testUser)
        assertEquals(false, result)

    }

    @Test
    fun `getUserId expected true`() {
        val userIdKey = Constants.USER_ID_KEY
        Mockito.`when`(sharedPreferences.getInt(userIdKey, -1)).thenReturn(2)

        val result = userRepository.getUserId()

        assertEquals(true, result == 2)
    }

    @Test
    fun `setUserId expected true`() {
        val userId = 2
        Mockito.`when`(editor.putInt(Constants.USER_ID_KEY, userId)).thenReturn(editor)

        userRepository.setUserId(userId)

        Mockito.verify(editor).putInt(Constants.USER_ID_KEY, userId)
        Mockito.verify(editor).apply()
    }

    @Test
    fun `test clearUserId expected true`() {
        Mockito.`when`(editor.remove(Constants.USER_ID_KEY)).thenReturn(editor)
        userRepository.clearUserId()

        Mockito.verify(editor).remove(Constants.USER_ID_KEY)
        Mockito.verify(editor).apply()
    }
}