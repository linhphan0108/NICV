package com.linhphan.presentation.di

import android.content.Context
import com.linhphan.presentation.WeatherForecastApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {
    @Provides
    @Singleton
    fun provideApplication(): Context {
        return WeatherForecastApp.instance
    }
}