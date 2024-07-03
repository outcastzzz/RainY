package com.example.rainy.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("name")
    val name: String,
    @SerialName("lat")
    val lat: Float,
    @SerialName("lon")
    val long: Float,
    @SerialName("country")
    val country: String,
)


