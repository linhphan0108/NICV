package com.linhphan.data.di

import android.content.Context
import com.linhphan.data.local.ForecastDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Singleton
    @Provides
    fun provideLocalDB(context: Context): ForecastDB {
        return ForecastDB.build(context)
    }
}