package com.example.rainy.presentation.weather

import kotlinx.coroutines.flow.StateFlow

interface WeatherComponent {

    val model: StateFlow<WeatherStore.State>

}