package com.example.rainy.presentation.forecast

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class ForecastComponentImpl @AssistedInject constructor(
    private val storeFactory: ForecastStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("forecast") private val forecast: com.example.domain.entity.Weather
): ForecastComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(
        forecast.forecast.forecastDay[0],
        forecast.forecast,
        stateKeeper
    ) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ForecastStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("forecast") forecast: com.example.domain.entity.Weather
        ): ForecastComponentImpl
    }

}