package com.example.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    val lat: Float,
    val lon: Float,
    @SerialName("display_name")
    val name: String,
)

