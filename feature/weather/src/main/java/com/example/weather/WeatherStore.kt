package com.example.weather

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.domain.entity.Astronomy
import com.example.domain.entity.Weather
import com.example.weather.WeatherStore.Intent
import com.example.weather.WeatherStore.Label
import com.example.weather.WeatherStore.State
import javax.inject.Inject

interface WeatherStore : Store<Intent, State, Label> {

    sealed interface Intent

    data class State(
        val weather: Weather,
        val astronomy: Astronomy
    )

    sealed interface Label
}

class WeatherStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(
        weather: Weather,
        astronomy: Astronomy
    ): WeatherStore =
        object : WeatherStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = State(
                weather = weather,
                astronomy = astronomy
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = WeatherStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class WeatherLoaded(
            val weather: Weather,
            val astronomy: Astronomy
        ): Action

    }

    private sealed interface Msg {

        data class WeatherLoaded(
            val weather: Weather,
            val astronomy: Astronomy
        ): Msg

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {}
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
