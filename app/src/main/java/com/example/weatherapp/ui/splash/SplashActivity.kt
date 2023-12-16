package com.example.weatherapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.common.utils.Constants.SPLASH_TIME_DELAY
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.ui.auth.AuthActivity
import com.example.weatherapp.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepositoryImpl
    private lateinit var navigationIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            setNavigation()
        }, SPLASH_TIME_DELAY)
    }

    private fun setNavigation() {
        navigationIntent = if (isUserLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }
        startActivity(navigationIntent)
        finish()
    }

    private fun isUserLoggedIn(): Boolean {
        val userId = userRepository.getUserId()
        return userId > 0
    }
}