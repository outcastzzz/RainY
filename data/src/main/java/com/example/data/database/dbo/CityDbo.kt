package com.example.data.database.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityDbo(
    @PrimaryKey
    val name: String,
    val lat: Float,
    val long: Float,
    val country: String,
)