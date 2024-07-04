package com.example.rainy.presentation.main

import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.coroutines.flow.StateFlow

interface MainComponent: BackHandlerOwner {

    val model: StateFlow<MainStore.State>

    fun onClickSelectCity()

    fun onClickSettings()
}