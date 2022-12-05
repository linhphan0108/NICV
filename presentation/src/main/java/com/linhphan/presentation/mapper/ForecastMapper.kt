package com.linhphan.presentation.mapper

import android.content.Context
import com.linhphan.domain.entity.ForecastEntity
import com.linhphan.presentation.R
import com.linhphan.presentation.model.ForecastModel


fun ForecastEntity.toWeatherInfoModel(context: Context): ForecastModel {
    return ForecastModel(
        date = context.getString(R.string.lp_text_date, date),
        avgTemp = context.getString(R.string.lp_text_avg_temp, avgTemp),
        humidity = context.getString(R.string.lp_text_humidity, humidity),
        pressure = context.getString(R.string.lp_text_pressure, pressure),
        desc = context.getString(R.string.lp_text_desc, desc)
    )
}