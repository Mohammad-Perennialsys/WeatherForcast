package com.example.weatherapp.data.repository

import android.content.SharedPreferences
import com.example.weatherapp.common.utils.Constants
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val sharedPreference: SharedPreferences,
    private val userDao: UserDao,
) : UserRepository {
    override fun getUserId(): Int {
        return sharedPreference.getInt(Constants.USER_ID_KEY, -1)
    }

    override fun setUserId(uid: Int) {
        val editor = sharedPreference.edit()
        editor.putInt(Constants.USER_ID_KEY, uid)
            .apply()
    }

    override fun clearUserId() {
        sharedPreference.edit().apply {
            remove(Constants.USER_ID_KEY)
            apply()
        }
    }
    override suspend fun getUserByEmail(email: String): UserEntity? {
        return withContext(dispatcher) {
            userDao.getUserByEmail(email = email)
        }
    }

    override suspend fun saveUser(user: UserEntity): Boolean {
        return withContext(dispatcher) {
            val userId = userDao.insert(user = user)
            userId > 0
        }
    }
}