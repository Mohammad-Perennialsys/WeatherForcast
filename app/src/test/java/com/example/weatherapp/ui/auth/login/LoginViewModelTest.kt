package com.example.weatherapp.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.UserEntity
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LoginViewModelTest {

    @get:Rule
    val coroutineDispatcherRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var userRepository: UserRepositoryImpl

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(userRepository = userRepository)
    }

    @Test
    fun `loginUser with empty fields expected error_field_required`() = runTest {

        loginViewModel.loginUser(email = "", password = "")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val emailError = loginViewModel.emailError.getOrAwaitValue()
        val passwordError = loginViewModel.passwordError.getOrAwaitValue()

        assertEquals(R.string.error_field_required, emailError)
        assertEquals(R.string.error_field_required, passwordError)
    }

    @Test
    fun `loginUser with invalid email expected error_invalid_email`() = runTest {

        loginViewModel.loginUser(email = "invalid.email", password = "Pass@123")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val result = loginViewModel.emailError.getOrAwaitValue()

        assertEquals(R.string.error_invalid_email, result)

    }

    @Test
    fun `loginUser with unregistered email expected error_no_user_with_email`() = runTest {

        Mockito.`when`(userRepository.getUserByEmail(ArgumentMatchers.anyString())).thenReturn(null)

        loginViewModel.loginUser(email = "test@test.com", password = "Pass@123")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val emailError = loginViewModel.emailError.getOrAwaitValue()

        assertEquals(R.string.error_no_user_with_email, emailError)
    }

    @Test
    fun `loginUser with wrong password expected error_wrong_password`() = runTest {
        val userToLogin = UserEntity(
            id = 1,
            username = "TestUser",
            email = "test@test.com",
            password = "Pass@123"
        )
        Mockito.`when`(userRepository.getUserByEmail(Mockito.anyString())).thenReturn(userToLogin)

        loginViewModel.loginUser(email = "test@test.com", password = "Pass@1234")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val result = loginViewModel.passwordError.getOrAwaitValue()
        assertEquals(R.string.error_wrong_password, result)
    }

    @Test
    fun `loginUser with valid credentials expected login success`() = runTest {
        val userToLogin = UserEntity(
            id = 1,
            username = "TestUser",
            email = "test@test.com",
            password = "Pass@123"
        )
        Mockito.`when`(userRepository.getUserByEmail(Mockito.anyString())).thenReturn(userToLogin)

        loginViewModel.loginUser(email = "test@test.com", password = "Pass@123")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val result = loginViewModel.currentLoginUser.getOrAwaitValue()

        assertEquals(1, result.id)
    }

    @Test
    fun `test resetErrors expected true`() {
        loginViewModel.resetErrors()
        val emailError = loginViewModel.emailError.getOrAwaitValue()
        val passError = loginViewModel.passwordError.getOrAwaitValue()
        assertNull(emailError)
        assertNull(passError)
    }
}