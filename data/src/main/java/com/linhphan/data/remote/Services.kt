package com.linhphan.data.remote

import com.linhphan.data.entity.ListForecastResponse
import com.linhphan.data.entity.TempUnit
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("data/2.5/forecast/daily")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("cnt") count: Int,
        @Query("appid") appId: String,
        @Query("units") units: String = TempUnit.CELSIUS.value
    ): ListForecastResponse
}