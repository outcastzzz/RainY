package com.example.forecast

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.domain.entity.ForecastDay
import com.example.domain.entity.ForecastObject
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface ForecastStore : Store<ForecastStore.Intent, ForecastStore.State, ForecastStore.Label> {

    sealed interface Intent

    @Serializable
    data class State(
        val hourlyForecast: ForecastDay,
        val dailyForecast: ForecastObject
    )

    sealed interface Label
}

class ForecastStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(
        hourlyForecast: ForecastDay,
        dailyForecast: ForecastObject,
        stateKeeper: StateKeeper
    ): ForecastStore =
        object : ForecastStore, Store<ForecastStore.Intent, ForecastStore.State, ForecastStore.Label> by storeFactory.create(
            name = "ForecastStore",
            initialState = stateKeeper.consume(
                key = "ForecastStoreState",
                strategy = ForecastStore.State.serializer()
            ) ?: ForecastStore.State(hourlyForecast, dailyForecast),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}.also {
            stateKeeper.register(key = "ForecastStoreState", strategy = ForecastStore.State.serializer()) {
                it.state.copy(hourlyForecast = hourlyForecast, dailyForecast = dailyForecast)
            }
        }

    private sealed interface Action {

        data class ForecastLoaded(
            val hourlyForecast: ForecastDay,
            val dailyForecast: ForecastObject
        ): Action

    }

    private sealed interface Msg {

        data class ForecastLoaded(
            val hourlyForecast: ForecastDay,
            val dailyForecast: ForecastObject
        ): Msg

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<ForecastStore.Intent, Action, ForecastStore.State, Msg, ForecastStore.Label>() {
        override fun executeAction(action: Action) {
            when(action) {
                is Action.ForecastLoaded -> dispatch(Msg.ForecastLoaded(
                    action.hourlyForecast,
                    action.dailyForecast
                ))
            }
        }
    }

    private object ReducerImpl : Reducer<ForecastStore.State, Msg> {
        override fun ForecastStore.State.reduce(msg: Msg): ForecastStore.State = when(msg) {
            is Msg.ForecastLoaded -> copy(
                hourlyForecast = msg.hourlyForecast,
                dailyForecast = msg.dailyForecast
            )
        }
    }
}
