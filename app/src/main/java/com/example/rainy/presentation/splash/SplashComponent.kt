package com.example.rainy.presentation.splash

import com.example.rainy.domain.entity.Astronomy
import com.example.rainy.domain.entity.Forecast
import com.example.rainy.domain.entity.InfoData
import com.example.rainy.domain.entity.Weather
import kotlinx.coroutines.flow.StateFlow

interface SplashComponent {

    val model: StateFlow<SplashStore.State>

    fun dataLoaded(data: InfoData)

}