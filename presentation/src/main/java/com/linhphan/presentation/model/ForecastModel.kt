package com.linhphan.presentation.model

import com.linhphan.presentation.feature.home.viewholder.ForecastViewHolderFactory

class ForecastModel(
    val date: String,
    val avgTemp: String,
    val pressure: String,
    val humidity: String,
    val desc: String,
    override val viewType: Int = ForecastViewHolderFactory.VIEW_TYPE_DAILY_FORECAST
): Displayable