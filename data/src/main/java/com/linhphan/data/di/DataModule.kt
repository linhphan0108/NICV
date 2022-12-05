package com.linhphan.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.linhphan.data.local.ForecastDB
import com.linhphan.data.remote.Services
import com.linhphan.data.repository.ForecastRepository
import com.linhphan.domain.repository.IForecastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideForecastData(
        db: ForecastDB, service: Services, gson: Gson,
        @Named("appId") appId: String, ioDispatcher: CoroutineDispatcher): IForecastRepository{
        return ForecastRepository(db, service, gson, appId, ioDispatcher)
    }

}