package com.linhphan.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.linhphan.data.entity.ForecastResponse
import com.linhphan.data.entity.TempResponse
import com.linhphan.data.entity.WeatherResponse
import com.linhphan.data.extensions.*
import com.linhphan.data.extensions.millisToStringDate
import com.linhphan.data.local.TblForecast
import com.linhphan.domain.entity.ForecastEntity

fun ForecastResponse.toForecastEntity(): ForecastEntity {
    return ForecastEntity(
        date = dtInSeconds.secondsToStringDate(),
        avgTemp = temp.getAvgTemp(),
        humidity = humidity.toPercent(),
        pressure = "$pressure%",
        desc = weathers.joinToString { weather -> weather.description }
    )
}

fun ForecastResponse.toTblForecast(cityName: String, gson: Gson): TblForecast {
    return TblForecast(
        cityName = cityName,
        date = dtInSeconds * 1000,
        temp = temp.toJson(gson),
        humidity = humidity,
        pressure = pressure,
        weathers = weathers.toJson(gson)
    )
}

fun TblForecast.toForecastEntity(gson: Gson): ForecastEntity {
    return ForecastEntity(
        date = date.millisToStringDate(),
        avgTemp = gson.fromJson(temp, TempResponse::class.java).getAvgTemp(),
        humidity = humidity.toPercent(),
        pressure = "$pressure%",
        desc = (gson.fromJson(
            weathers, object : TypeToken<List<WeatherResponse>?>() {}.type
        ) as List<WeatherResponse>).joinToString { weather -> weather.description }
    )
}