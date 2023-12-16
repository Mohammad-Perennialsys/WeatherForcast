package com.example.weatherapp.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.validation.ValidationHelper
import com.example.weatherapp.data.local.entity.UserEntity
import com.example.weatherapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _usernameError: MutableLiveData<Int?> = MutableLiveData()
    val usernameError: LiveData<Int?> = _usernameError

    private val _emailError: MutableLiveData<Int?> = MutableLiveData()
    val emailError: LiveData<Int?> = _emailError

    private val _passwordError: MutableLiveData<Int?> = MutableLiveData()
    val passwordError: LiveData<Int?> = _passwordError

    private val _confirmPasswordError: MutableLiveData<Int?> = MutableLiveData()
    val confirmPasswordError: LiveData<Int?> = _confirmPasswordError

    private val _isSignupSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSignupSuccess: LiveData<Boolean> get() = _isSignupSuccess

    fun signupUser(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val validateUserName = ValidationHelper.validateUserName(userName)
        val validateEmail = ValidationHelper.validateEmail(email)
        val validatePassword = ValidationHelper.validatePassword(password)
        val validateRepeatedPassword = ValidationHelper.validateRepeatedPassword(
            pass = password,
            repeatedPass = confirmPassword
        )
        if (!validateUserName.isValid) {
            _usernameError.value = validateUserName.error
        }
        if (!validateEmail.isValid) {
            _emailError.value = validateEmail.error
        }
        if (!validatePassword.isValid) {
            _passwordError.value = validatePassword.error
        }
        if (!validateRepeatedPassword.isValid) {
            _confirmPasswordError.value = validateRepeatedPassword.error
        }

        if (validateUserName.isValid && validateEmail.isValid
            && validatePassword.isValid && validateRepeatedPassword.isValid
        ) {
            viewModelScope.launch {
                val result = userRepository.saveUser(
                    user = UserEntity(
                        username = userName,
                        email = email,
                        password = password
                    )
                )
                _isSignupSuccess.postValue(result)
            }
        }
    }

    fun resetErrors() {
        _usernameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }
}