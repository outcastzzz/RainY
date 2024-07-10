package com.example.domain.useCase

import com.example.domain.repository.ForecastRepository
import javax.inject.Inject

class LoadWeatherForCityExplicitUseCase @Inject constructor(
    private val repository: ForecastRepository
) {

    suspend operator fun invoke(lat: Float, long: Float) = repository
        .loadWeatherForCityExplicit(lat, long)

}