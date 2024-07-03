package com.example.rainy.presentation.forecast

import kotlinx.coroutines.flow.StateFlow

interface ForecastComponent {

    val model: StateFlow<ForecastStore.State>

}