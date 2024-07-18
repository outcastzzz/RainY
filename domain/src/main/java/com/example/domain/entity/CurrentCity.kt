package com.example.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class CurrentCity(
    val lat: Float,
    val lon: Float,
    val address: Address
)

@Serializable
data class Address(
    val city: String,
    val country: String,
    val state: String
)

