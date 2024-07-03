package com.example.rainy.domain.useCase

import com.example.rainy.domain.repository.ForecastRepository
import javax.inject.Inject

class LoadAstronomyDataUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {

    suspend operator fun invoke(lat: Float, long: Float) = forecastRepository
        .loadAstronomyData(lat, long)

}