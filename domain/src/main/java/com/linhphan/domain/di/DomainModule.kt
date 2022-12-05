package com.linhphan.domain.di

import com.linhphan.domain.usecase.IForecastUseCase
import com.linhphan.domain.usecase.ForecastUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindForecastUseCase(impl: ForecastUseCase): IForecastUseCase
}