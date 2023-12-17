package com.example.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.weatherapp.common.utils.Constants.SHARED_PREFERENCE
import com.example.weatherapp.data.local.dao.UserDao
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.remote.api.ApiService
import com.example.weatherapp.data.repository.UserRepository
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMasterKey(@ApplicationContext context: Context): MasterKey {
        return MasterKey.Builder(context).apply {
            setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        }.build()
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreference(
        @ApplicationContext context: Context,
        masterKey: MasterKey
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        dispatcher: CoroutineDispatcher,
        sharedPreferences: SharedPreferences,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(
            dispatcher = dispatcher,
            sharedPreference = sharedPreferences,
            userDao = userDao
        )
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(
        dispatcher: CoroutineDispatcher,
        apiService: ApiService,
        weatherDao: WeatherDao,
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            dispatcher = dispatcher,
            apiService = apiService,
            weatherDao = weatherDao,
        )
    }

}