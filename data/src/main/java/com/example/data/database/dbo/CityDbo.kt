package com.example.data.database.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val lat: Float,
    val lon: Float,
)

