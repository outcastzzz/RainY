package com.example.rainy.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Astronomy(
    val location: Location,
    val astronomy: AstronomyObject
)

@Serializable
data class AstronomyObject(
    val astro: Astro
)

@Serializable
data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerialName("moon_phase")
    val moonPhase: String
)
