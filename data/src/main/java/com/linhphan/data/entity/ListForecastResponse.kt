package com.linhphan.data.entity

import com.google.gson.annotations.SerializedName

data class ListForecastResponse(
    @SerializedName("city") val city: City,
    @SerializedName("cnt") val cnt: Int,
    @SerializedName("list") val forecasts: List<ForecastResponse>
)


data class City (
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("coord") var coord : Coord,
    @SerializedName("country") var country : String,
    @SerializedName("population") var population : Int,
    @SerializedName("timezone") var timezone : Int

)


data class Coord (
    @SerializedName("lon") var lon : Double,
    @SerializedName("lat") var lat : Double
)

data class ForecastResponse(
    @SerializedName("dt") val dtInSeconds: Long,//in seconds not milliseconds
    @SerializedName("temp") val temp: TempResponse,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("weather") val weathers: List<WeatherResponse>
)

data class TempResponse(
    @SerializedName("day") val day: Float,
    @SerializedName("min") val min: Float,
    @SerializedName("max") val max: Float,
    @SerializedName("night") val night: Float,
    @SerializedName("eve") val eve: Float,
    @SerializedName("morn") val morn: Float,
)

data class WeatherResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)
