package com.linhphan.domain.usecase

import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.domain.repository.IForecastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IForecastUseCase {

    /**
     * try to fetch any forecasts at the specified [cityName]
     */
    suspend fun getForecast(cityName: String, count: Int = 7): Flow<ResultWrapper<List<ForecastEntity>>>
}

class ForecastUseCase @Inject constructor(private val repository: IForecastRepository) :
    IForecastUseCase {

    override suspend fun getForecast(
        cityName: String,
        count: Int
    ): Flow<ResultWrapper<List<ForecastEntity>>> {
        return repository.getForecast(cityName)
    }
}