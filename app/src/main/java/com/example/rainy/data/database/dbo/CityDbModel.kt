package com.example.rainy.data.database.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityDbModel(
    @PrimaryKey
    val name: String,
    val lat: Float,
    val long: Float,
    val country: String,
)
