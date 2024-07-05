package com.example.settings

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.settings.SettingsStore.Intent
import com.example.settings.SettingsStore.Label
import com.example.settings.SettingsStore.State
import javax.inject.Inject

interface SettingsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack: Intent
    }

    data object State

    sealed interface Label {
        data object ClickBack: Label
    }
}

class SettingsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): SettingsStore =
        object : SettingsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SettingsStore",
            initialState = State,
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) = when(intent) {
            Intent.ClickBack -> publish(Label.ClickBack)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = State
    }
}
