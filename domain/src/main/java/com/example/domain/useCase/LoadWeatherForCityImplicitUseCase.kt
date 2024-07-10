package com.example.domain.useCase

import com.example.domain.repository.ForecastRepository
import javax.inject.Inject

class LoadWeatherForCityImplicitUseCase @Inject constructor(
    private val repository: ForecastRepository
) {

    suspend operator fun invoke(lat: Float, long: Float) = repository
        .loadWeatherForCityImplicit(lat, long)

}