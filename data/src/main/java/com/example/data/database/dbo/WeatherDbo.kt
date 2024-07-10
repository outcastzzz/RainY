package com.example.data.database.dbo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @Embedded(prefix = "location_")
    val location: LocationEntity,
    @Embedded(prefix = "current_")
    val current: CurrentEntity,
    @Embedded(prefix = "forecast_")
    val forecast: ForecastEntity
)

@Entity
data class LocationEntity(
    val name: String,
    val region: String,
    val country: String,
    val epoch: Long
)

@Entity
data class CurrentEntity(
    val lastUpdated: Long,
    val tempC: Double,
    @Embedded(prefix = "condition_")
    val condition: ConditionEntity,
    val windKph: Double,
    val windDir: String,
    val pressureMb: Float,
    val precipMm: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike: Double,
    val visibility: Float,
    val uv: Double
)

@Entity
data class ConditionEntity(
    val text: String,
    val code: Int
)

@Entity
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val forecastday: List<ForecastDayEntity>
)

@Entity
data class ForecastDayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    @Embedded(prefix = "day_")
    val day: DayEntity,
    @Embedded(prefix = "astro_")
    val astro: AstroEntity,
    val hour: List<HourEntity>
)

@Entity
data class DayEntity(
    val maxtemp: Double,
    val mintemp: Double,
    val avgtemp: Double,
    val totalPrecip: Double,
    val avgVisibility: Double,
    val avghumidity: Int,
    val rainChance: Int,
    @Embedded(prefix = "condition_")
    val condition: ConditionEntity,
    val uv: Double
)

@Entity
data class HourEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: Long,
    val temp: Double,
    @Embedded(prefix = "condition_")
    val condition: ConditionEntity
)

@Entity
data class AstronomyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @Embedded(prefix = "location_")
    val location: LocationEntity,
    @Embedded(prefix = "astronomy_")
    val astronomy: AstronomyObjectEntity
)

@Entity
data class AstronomyObjectEntity(
    @Embedded(prefix = "astro_")
    val astro: AstroEntity
)

@Entity
data class AstroEntity(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonPhase: String
)
