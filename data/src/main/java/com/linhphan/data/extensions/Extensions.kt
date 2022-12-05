package com.linhphan.data.extensions

import com.google.gson.Gson
import com.linhphan.common.Logger
import com.linhphan.data.entity.TempResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val CELSIUS_SYMBOL = "\u2103"
private const val DATE_FORMAT = "EEE, dd MMM yyyy"

fun Long.secondsToStringDate(): String {
    return (this * 1000).millisToStringDate()
}

fun Long.millisToStringDate(): String {
    return try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val date = Date(this)
        simpleDateFormat.format(date)
    }catch (e: ParseException){
        Logger.e("Long.toStringDate", e.message, e)
        ""
    }
}

fun String.toDate(): Date?{
    return try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.parse(this)
    }catch (e: ParseException){
        Logger.e("Long.toStringDate", e.message, e)
        null
    }
}

fun Int.toPercent(): String {
    return "$this%"
}

fun TempResponse.getAvgTemp(): String {
    return "${String.format("%.1f", (max + min) * 0.5f)} $CELSIUS_SYMBOL"
}

fun Any.toJson(gson: Gson): String {
    return gson.toJson(this)
}