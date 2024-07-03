package com.example.rainy.presentation.main

import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.example.rainy.domain.entity.Astronomy
import com.example.rainy.domain.entity.Forecast
import com.example.rainy.domain.entity.InfoData
import com.example.rainy.domain.entity.Weather
import kotlinx.coroutines.flow.StateFlow

interface MainComponent: BackHandlerOwner {

    val model: StateFlow<MainStore.State>

    fun onClickSelectCity()

    fun onClickSettings()
}