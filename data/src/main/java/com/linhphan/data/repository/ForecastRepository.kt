package com.linhphan.data.repository

import com.google.gson.Gson
import com.linhphan.common.DateTimeUtil
import com.linhphan.common.Logger
import com.linhphan.data.entity.ListForecastResponse
import com.linhphan.data.extensions.toDate
import com.linhphan.data.local.ForecastDB
import com.linhphan.data.mapper.toForecastEntity
import com.linhphan.data.mapper.toTblForecast
import com.linhphan.data.remote.Services
import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.domain.repository.IForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ForecastRepository @Inject constructor(
    private val db: ForecastDB,
    private val services: Services,
    private val gson: Gson,
    @Named("appId") private val appId: String,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseRepository(ioDispatcher), IForecastRepository {
    override suspend fun getForecast(
        cityName: String,
        count: Int
    ): Flow<ResultWrapper<List<ForecastEntity>>> {
        return safeCall(
            dbCall = {
                var listForecastEntity: List<ForecastEntity>? = null
                val cachedForecasts = db.getForecastDao().queryForecast(cityName)
                if (!cachedForecasts.isNullOrEmpty()){
                    listForecastEntity = cachedForecasts.map { entity -> entity.toForecastEntity(gson) }
                }
                Logger.i("ForecastRepository", "forecasts from db ${listForecastEntity?.count()}")
                listForecastEntity
            },
            apiCall = {
                deleteOutDateForecasts()
                val response = services.getForecast(
                    query = cityName,
                    count = count,
                    appId = appId
                )
                storeForecasts(cityName, response, gson)
                response
            },
            forceApiCall = {
                return@safeCall canRefreshForecast(it.firstOrNull())
            },
            mapper = { listForecastResponse ->
                listForecastResponse.forecasts.map {
                    it.toForecastEntity()
                }
            }
        )
    }

    /**
     * @return true if there is at least forecast is outdated.
     * otherwise return false.
     */
    private fun canRefreshForecast(firstForecast: ForecastEntity?): Boolean {
        if (firstForecast == null){
            return true
        }
        val today = DateTimeUtil.getTodayZeroHundredHourInDate()
        val firstForecastDate = firstForecast.date.toDate() ?: return true

        return today.after(firstForecastDate)
    }

    /**
     * cache the forecasts that are just fetched from server into local database.
     */
    private suspend fun storeForecasts(cityName: String, listForecastResponse: ListForecastResponse, gson: Gson) {
        val forecasts = listForecastResponse.forecasts.map { it.toTblForecast(cityName, gson) }
        val count = db.getForecastDao().storeForecasts(forecasts).count()
        Logger.i("ForecastRepository", "store forecasts down to db | size = $count")
    }

    /**
     * try to delete forecasts of the past days.
     */
    private fun deleteOutDateForecasts(){
        val todayInSeconds = DateTimeUtil.getTodayZeroHundredHourInMillis()
        val deletedItemCount = db.getForecastDao().deleteForecasts(todayInSeconds)
        Logger.i("ForecastRepository", "outdated forecasts deleted | size = $deletedItemCount")
    }
}