package com.example.splash

import com.example.domain.entity.InfoData
import kotlinx.coroutines.flow.StateFlow

interface SplashComponent {

    val model: StateFlow<SplashStore.State>

    fun dataLoaded(data: InfoData)

}