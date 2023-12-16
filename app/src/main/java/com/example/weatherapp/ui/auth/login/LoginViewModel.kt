package com.example.weatherapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.common.validation.ValidationHelper
import com.example.weatherapp.data.local.entity.UserEntity
import com.example.weatherapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _emailError: MutableLiveData<Int?> = MutableLiveData()
    val emailError: LiveData<Int?> = _emailError

    private val _passwordError: MutableLiveData<Int?> = MutableLiveData()
    val passwordError: LiveData<Int?> = _passwordError

    private val _currentLoginUser = MutableLiveData<UserEntity>()
    val currentLoginUser: LiveData<UserEntity> get() = _currentLoginUser

    fun loginUser(email: String, password: String) {
        val emailValidation = ValidationHelper.validateEmail(email)
        val passwordValidation = ValidationHelper.validatePassword(password)
        if (!emailValidation.isValid) {
            _emailError.value = emailValidation.error
        }
        if (!passwordValidation.isValid) {
            _passwordError.value = passwordValidation.error
        }
        if (emailValidation.isValid && passwordValidation.isValid) {
            viewModelScope.launch {
                val result = userRepository.getUserByEmail(email)
                result?.let { user ->
                    if (user.password != password) {
                        _passwordError.postValue(R.string.error_wrong_password)
                    } else {
                        userRepository.setUserId(user.id)
                        _currentLoginUser.postValue(user)
                    }
                } ?: _emailError.postValue(R.string.error_no_user_with_email)
            }
        }
    }

    fun resetErrors() {
        _emailError.value = null
        _passwordError.value = null
    }
}