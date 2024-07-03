package com.example.rainy.domain.repository

import com.example.rainy.domain.entity.Astronomy
import com.example.rainy.domain.entity.Forecast
import com.example.rainy.domain.entity.Weather

interface ForecastRepository {

    suspend fun loadWeatherForCity(lat: Float, long: Float): Weather

    suspend fun getCurrentCityName(lat: Float, long: Float): String

    suspend fun loadAstronomyData(lat: Float, long: Float): Astronomy

    suspend fun loadTodayHourlyForecast(lat: Float, long: Float): Forecast

}