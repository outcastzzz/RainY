package com.example.rainy.presentation.forecast

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.rainy.presentation.forecast.ForecastStore.Intent
import com.example.rainy.presentation.forecast.ForecastStore.Label
import com.example.rainy.presentation.forecast.ForecastStore.State
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface ForecastStore : Store<Intent, State, Label> {

    sealed interface Intent

    @Serializable
    data class State(
        val hourlyForecast: com.example.domain.entity.ForecastDay,
        val dailyForecast: com.example.domain.entity.ForecastObject
    )

    sealed interface Label
}

class ForecastStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(
        hourlyForecast: com.example.domain.entity.ForecastDay,
        dailyForecast: com.example.domain.entity.ForecastObject,
        stateKeeper: StateKeeper
    ): ForecastStore =
        object : ForecastStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ForecastStore",
            initialState = stateKeeper.consume(
                key = "ForecastStoreState",
                strategy = State.serializer()
            ) ?: State(hourlyForecast, dailyForecast),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}.also {
            stateKeeper.register(key = "ForecastStoreState", strategy = State.serializer()) {
                it.state.copy(hourlyForecast = hourlyForecast, dailyForecast = dailyForecast)
            }
        }

    private sealed interface Action {

        data class ForecastLoaded(
            val hourlyForecast: com.example.domain.entity.ForecastDay,
            val dailyForecast: com.example.domain.entity.ForecastObject
        ): Action

    }

    private sealed interface Msg {

        data class ForecastLoaded(
            val hourlyForecast: com.example.domain.entity.ForecastDay,
            val dailyForecast: com.example.domain.entity.ForecastObject
        ): Msg

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when(action) {
                is Action.ForecastLoaded -> dispatch(Msg.ForecastLoaded(
                    action.hourlyForecast,
                    action.dailyForecast
                ))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.ForecastLoaded -> copy(
                hourlyForecast = msg.hourlyForecast,
                dailyForecast = msg.dailyForecast
            )
        }
    }
}
