package com.example.domain.useCase

import com.example.domain.repository.ForecastRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: ForecastRepository
) {

    suspend operator fun invoke(lat: Float, long: Float) = repository.getWeather(lat, long)

}