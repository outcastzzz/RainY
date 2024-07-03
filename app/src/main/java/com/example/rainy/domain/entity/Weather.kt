package com.example.rainy.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val location: Location,
    val current: Current,
    @SerialName("forecast")
    val forecast: ForecastObject
)

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    @SerialName("localtime_epoch")
    val epoch: Long
)

@Serializable
data class Current(
    @SerialName("last_updated_epoch")
    val lastUpdate: Long,
    @SerialName("temp_c")
    val tempC: Double,
    val condition: Condition,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("pressure_mb")
    val pressureMb: Float,
    @SerialName("precip_mm")
    val precip: Double,
    val humidity: Int,
    val cloud: Int,
    @SerialName("feelslike_c")
    val feelsLike: Double,
    @SerialName("vis_km")
    val visibility: Float,
    val uv: Double,
)

@Serializable
data class Condition(
    val text: String,
    val code: Int
)