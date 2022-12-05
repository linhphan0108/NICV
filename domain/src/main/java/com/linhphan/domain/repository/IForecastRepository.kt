package com.linhphan.domain.repository

import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface IForecastRepository {
    /**
     * try to fetch the forecasts for the specific [cityName]
     * @param cityName the city name
     * @param count the number days that we going to fetch the forecasts for
     */
    suspend fun getForecast(cityName: String, count: Int = 7): Flow<ResultWrapper<List<ForecastEntity>>>
}