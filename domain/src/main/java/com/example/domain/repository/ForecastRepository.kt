package com.example.domain.repository

import com.example.domain.entity.Astronomy
import com.example.domain.entity.Weather

interface ForecastRepository {

    suspend fun loadWeatherForCityImplicit(lat: Float, long: Float)

    suspend fun loadWeatherForCityExplicit(lat: Float, long: Float): Weather

    suspend fun getCurrentCityName(lat: Float, long: Float): String

    suspend fun loadAstronomyData(lat: Float, long: Float): Astronomy

    suspend fun getWeather(lat: Float, long: Float): Weather

}