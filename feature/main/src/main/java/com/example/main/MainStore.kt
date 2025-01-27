package com.example.main

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.domain.entity.InfoData
import com.example.main.MainStore.Intent
import com.example.main.MainStore.Label
import com.example.main.MainStore.State
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface MainStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickSelectCity: Intent

        data object ClickSettings: Intent

    }

    @Serializable
    data class State(val data: InfoData)

    sealed interface Label {

        data object ClickSelectCity: Label

        data object ClickSettings: Label

    }
}

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(
        infoData: InfoData,
        stateKeeper: StateKeeper
    ): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = stateKeeper.consume(
                key = "MainStoreState",
                strategy = State.serializer()
            ) ?: State(infoData),
            bootstrapper = BootstrapperImpl(),
            executorFactory = MainStoreFactory::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}.also {
            stateKeeper.register(key = "MainStoreState", strategy = State.serializer()) {
                it.state.copy(data = infoData)
            }
        }

    private sealed interface Action

    private sealed interface Msg {

        data class DataLoaded(val data: InfoData): Msg

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.ClickSelectCity -> publish(Label.ClickSelectCity)
                Intent.ClickSettings -> publish(Label.ClickSettings)
            }

        }

        override fun executeAction(action: Action) {}

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.DataLoaded -> copy(
                data = InfoData(
                    cityName = msg.data.cityName,
                    forecast = msg.data.forecast,
                    weather = msg.data.weather,
                    astronomy = msg.data.astronomy
                )
            )
        }
    }
}
