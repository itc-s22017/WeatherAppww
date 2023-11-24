package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class Main(
//    val main:List<forMain>,
//    val weather: List<Weather>,
//    val wind:List<Wind>,
//    val pop: Int,
//    @SerialName("dt_txt") val dtTxt:String
//
//)
//
//@Serializable
//data class forMain(
//    val temp:Int,
//    @SerialName("feels_like") val feelsLike:Int,
//    val pressure: Int,
//    val humidity: Int,
//)
//
//@Serializable
//data class Weather(
//    val description: String,
//    val icon: String
//)
//
//@Serializable
//data class Wind(
//    val speed:Int,
//    val deg: Int,
//    val gust: Int
//)

@Serializable
data class WeatherResponse(
    val list: List<WeatherData>
)

@Serializable
data class WeatherData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val pop: Double,
    @SerialName("dt_txt")
    val dateTime: String,
    val snow: Snow? = null
)

@Serializable
data class Main(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    val pressure: Double,
    val humidity: Double
)

@Serializable
data class Weather(
    val description: String,
    val icon: String
)

@Serializable
data class Wind(
    val speed: Double,
    val deg: Double,
    val gust: Double
)

@Serializable
data class Snow(
    @SerialName("3h")
    val snowVolume: Double
)