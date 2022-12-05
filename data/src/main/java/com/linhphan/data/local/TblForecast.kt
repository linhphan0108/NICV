package com.linhphan.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "tblForecast", primaryKeys = ["cityName", "date"])
data class TblForecast(
    val cityName: String,
    val date: Long,//milliseconds
    @ColumnInfo(name = "temperature")
    val temp: String,
    val pressure: Int,
    val humidity: Int,
    val weathers: String
)