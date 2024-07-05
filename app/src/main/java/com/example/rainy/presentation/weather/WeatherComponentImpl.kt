package com.example.rainy.presentation.weather

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.domain.entity.Astronomy
import com.example.domain.entity.Weather
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class WeatherComponentImpl @AssistedInject constructor(
    private val storeFactory: WeatherStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("weather") private val weather: com.example.domain.entity.Weather,
    @Assisted("astronomy") private val astronomy: com.example.domain.entity.Astronomy,
): WeatherComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(weather, astronomy) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<WeatherStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("weather") weather: com.example.domain.entity.Weather,
            @Assisted("astronomy") astronomy: com.example.domain.entity.Astronomy,
        ): WeatherComponentImpl

    }

}