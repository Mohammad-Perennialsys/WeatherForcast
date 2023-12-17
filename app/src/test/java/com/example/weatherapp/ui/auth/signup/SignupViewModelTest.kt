package com.example.weatherapp.ui.auth.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.UserEntity
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SignupViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var userRepository: UserRepositoryImpl

    lateinit var signupViewModel: SignupViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        signupViewModel = SignupViewModel(userRepository)
    }

    @Test
    fun `signupUser with empty string expected error_field_required`() = runTest {
        signupViewModel.signupUser(userName = "", email = "", password = "", confirmPassword = "")
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val usernameError = signupViewModel.usernameError.getOrAwaitValue()
        val emailError = signupViewModel.emailError.getOrAwaitValue()

        assertEquals(R.string.error_field_required, usernameError)
        assertEquals(R.string.error_field_required, emailError)

    }

    @Test
    fun `signupUser with invalid email expected error_invalid_email`() = runTest {
        signupViewModel.signupUser(
            userName = "Test1",
            email = "test.com",
            password = "",
            confirmPassword = ""
        )
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val emailError = signupViewModel.emailError.getOrAwaitValue()
        assertEquals(R.string.error_invalid_email, emailError)
    }

    @Test
    fun `signupUser with confirm password not matched with password expected error_pass_not_matched`() =
        runTest {
            signupViewModel.signupUser(
                userName = "Test1",
                email = "test@test.com",
                password = "Pass@123",
                confirmPassword = "Pass@1234"
            )
            coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            val confirmPasswordError = signupViewModel.confirmPasswordError.getOrAwaitValue()
            assertEquals(R.string.error_pass_not_matched, confirmPasswordError)
        }

    @Test
    fun `signupUser with valid form inputs expected true`() = runTest {

        val userToSave = UserEntity(
            username = "Test1",
            email = "test1@test.com",
            password = "Pass@123"
        )
        Mockito.`when`(
            userRepository.saveUser(userToSave)
        ).thenReturn(true)

        signupViewModel.signupUser(
            userName = "Test1",
            email = "test1@test.com",
            password = "Pass@123",
            confirmPassword = "Pass@123"
        )
        coroutineDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val result = signupViewModel.isSignupSuccess.getOrAwaitValue()
        assert(result)

    }

    @Test
    fun `test resetErrors expected true`() {
        signupViewModel.resetErrors()
        val emailError = signupViewModel.emailError.getOrAwaitValue()
        val passError = signupViewModel.passwordError.getOrAwaitValue()
        Assert.assertNull(emailError)
        Assert.assertNull(passError)
    }
}