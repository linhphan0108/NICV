package com.linhphan.domain.entity

data class ForecastEntity(
    val date: String,
    val avgTemp: String,
    val humidity: String,
    val pressure: String,
    val desc: String
)