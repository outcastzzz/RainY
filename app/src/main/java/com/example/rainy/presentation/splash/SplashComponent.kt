package com.example.rainy.presentation.splash

import kotlinx.coroutines.flow.StateFlow

interface SplashComponent {

    val model: StateFlow<SplashStore.State>

    fun dataLoaded(data: com.example.domain.entity.InfoData)

}