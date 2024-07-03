package com.example.rainy.domain.useCase

import com.example.rainy.domain.repository.ForecastRepository
import javax.inject.Inject

class LoadWeatherForCityUseCase @Inject constructor(
    private val repository: ForecastRepository
) {

    suspend operator fun invoke(lat: Float, long: Float) = repository
        .loadWeatherForCity(lat, long)

}