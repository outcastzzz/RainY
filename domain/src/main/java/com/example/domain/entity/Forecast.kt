package com.example.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDay>
)

@Serializable
data class ForecastDay(
    @SerialName("date_epoch")
    val date: Long,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>
)

@Serializable
data class Day(
    @SerialName("maxtemp_c")
    val maxTemp: Double,
    @SerialName("mintemp_c")
    val minTemp: Double,
    @SerialName("avgtemp_c")
    val avgTemp: Double,
    @SerialName("totalprecip_mm")
    val totalPrecip: Double,
    @SerialName("avgvis_km")
    val avgVisibility: Double,
    @SerialName("avghumidity")
    val avgHumidity: Int,
    @SerialName("daily_chance_of_rain")
    val rainChance: Int,
    @SerialName("condition")
    val condition: Condition,
    val uv: Double
)

@Serializable
data class Hour(
    @SerialName("time_epoch")
    val time: Long,
    @SerialName("temp_c")
    val temp: Double,
    val condition: Condition
)


