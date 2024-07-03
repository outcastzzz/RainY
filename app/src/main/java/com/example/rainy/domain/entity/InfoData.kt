package com.example.rainy.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class InfoData(
    val cityName: String,
    val forecast: ForecastDay,
    val weather: Weather,
    val astronomy: Astronomy
)
