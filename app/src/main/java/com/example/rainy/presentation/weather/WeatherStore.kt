package com.example.rainy.presentation.weather

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.rainy.presentation.weather.WeatherStore.Intent
import com.example.rainy.presentation.weather.WeatherStore.Label
import com.example.rainy.presentation.weather.WeatherStore.State
import javax.inject.Inject

interface WeatherStore : Store<Intent, State, Label> {

    sealed interface Intent

    data class State(
        val weather: com.example.domain.entity.Weather,
        val astronomy: com.example.domain.entity.Astronomy
    )

    sealed interface Label
}

class WeatherStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(
        weather: com.example.domain.entity.Weather,
        astronomy: com.example.domain.entity.Astronomy
    ): WeatherStore =
        object : WeatherStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = State(
                weather = weather,
                astronomy = astronomy
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class WeatherLoaded(
            val weather: com.example.domain.entity.Weather,
            val astronomy: com.example.domain.entity.Astronomy
        ): Action

    }

    private sealed interface Msg {

        data class WeatherLoaded(
            val weather: com.example.domain.entity.Weather,
            val astronomy: com.example.domain.entity.Astronomy
        ): Msg

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) = when(action) {
            is Action.WeatherLoaded -> dispatch(
                Msg.WeatherLoaded(
                    action.weather,
                    action.astronomy
                )
            )
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.WeatherLoaded -> copy(weather = msg.weather, astronomy = msg.astronomy)
        }
    }
}
