package com.example.rainy.presentation.splash

import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.rainy.domain.entity.InfoData
import com.example.rainy.domain.useCase.GetCurrentCityNameUseCase
import com.example.rainy.domain.useCase.LoadAstronomyDataUseCase
import com.example.rainy.domain.useCase.LoadTodayHourlyForecastUseCase
import com.example.rainy.domain.useCase.LoadWeatherForCityUseCase
import com.example.rainy.presentation.splash.SplashStore.Intent
import com.example.rainy.presentation.splash.SplashStore.Label
import com.example.rainy.presentation.splash.SplashStore.State
import com.example.rainy.presentation.splash.SplashStore.State.SplashState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

interface SplashStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class DataLoaded(
            val data: InfoData
        ): Intent

    }

    @Serializable
    data class State(
        val splashState: SplashState,
    ) {

        @Serializable
        sealed interface SplashState {

            @Serializable
            data object Initial: SplashState

            @Serializable
            data object Loading: SplashState

            @Serializable
            data object Error: SplashState

            @Serializable
            data class SuccessLoaded(
                val data: InfoData
            ): SplashState

        }

    }

    sealed interface Label {

        data class DataLoaded(
            val data: InfoData
        ): Label

    }
}

class SplashStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCurrentCityNameUseCase: GetCurrentCityNameUseCase,
    private val loadWeatherForCityUseCase: LoadWeatherForCityUseCase,
    private val loadAstronomyDataUseCase: LoadAstronomyDataUseCase
) {

    fun create(
        lat: Float,
        long: Float,
        stateKeeper: StateKeeper
    ): SplashStore =
        object : SplashStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SplashStore",
            initialState = stateKeeper.consume(
                key = "SplashStoreState",
                strategy = State.serializer()
            ) ?: State(splashState = SplashState.Initial),
            bootstrapper = BootstrapperImpl(lat, long),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}.also {
            stateKeeper.register(key = "SplashStoreState", strategy = State.serializer()) {
                it.state.copy(splashState = SplashState.Initial)
            }
        }

    private sealed interface Action {

        data object DataLoading: Action

        data object DataError: Action

        data class DataLoaded(
            val data: InfoData
        ): Action

    }

    private sealed interface Msg {

        data object DataLoading: Msg

        data object DataError: Msg

        data class DataLoaded(
            val data: InfoData
        ): Msg

    }

    private inner class BootstrapperImpl(
        private val lat: Float,
        private val long: Float,
    ): CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.DataLoading)
                try {
                    val cityName = getCurrentCityNameUseCase(lat, long)
                    val weather = loadWeatherForCityUseCase(lat, long)
                    val astronomy = loadAstronomyDataUseCase(lat, long)
                    dispatch(Action.DataLoaded(InfoData(cityName, weather.forecast.forecastDay[0], weather, astronomy)))
                } catch (e: Exception) {
                    dispatch(Action.DataError)
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) = when(intent) {
            is Intent.DataLoaded -> publish(Label.DataLoaded(
                intent.data
            ))
        }


        override fun executeAction(action: Action) = when(action) {
            Action.DataError -> dispatch(Msg.DataError)
            is Action.DataLoaded -> dispatch(Msg.DataLoaded(
                action.data
            ))
            Action.DataLoading -> dispatch(Msg.DataLoading)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            Msg.DataError -> copy(splashState = SplashState.Error)
            is Msg.DataLoaded -> copy(splashState = SplashState.SuccessLoaded(
                msg.data
            ))
            Msg.DataLoading -> copy(splashState = SplashState.Loading)
        }
    }
}
