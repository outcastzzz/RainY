package com.example.weather

import kotlinx.coroutines.flow.StateFlow

interface WeatherComponent {

    val model: StateFlow<WeatherStore.State>

}