package com.example.selectcity

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.common.utils.findIconForCode
import com.example.domain.entity.City
import com.example.domain.useCase.ChangeFavouriteStateUseCase
import com.example.domain.useCase.GetFavouriteCitiesUseCase
import com.example.domain.useCase.LoadWeatherForCityExplicitUseCase
import com.example.selectcity.SelectCityStore.Intent
import com.example.selectcity.SelectCityStore.Label
import com.example.selectcity.SelectCityStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SelectCityStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack: Intent

        data object ClickAddCity: Intent

        data class SwipeRemoveCity(val city: City): Intent

    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val cityItemState: CityItemState
        )

        sealed interface CityItemState {

            data object Initial: CityItemState

            data object Loading: CityItemState

            data object Error: CityItemState

            data class Loaded(
                val cityName: String,
                val temp: Int,
                val description: String,
                val icon: Int
            ): CityItemState

        }

    }

    sealed interface Label {

        data object ClickBack: Label

        data object ClickAddCity: Label

        data class SwipeRemoveCity(val city: City): Label

    }
}

class SelectCityStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val loadWeatherForCityExplicitUseCase: LoadWeatherForCityExplicitUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase
) {

    fun create(): SelectCityStore =
        object : SelectCityStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SelectCityStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class CitiesLoaded(val cities: List<City>): Action

    }

    private sealed interface Msg {

        data class CitiesLoaded(val cities: List<City>): Msg

        data class WeatherLoaded(
            val cityName: String,
            val temp: Int,
            val description: String,
            val icon: Int
        ): Msg

        data class WeatherError(val cityName: String): Msg

        data class WeatherLoading(val cityName: String): Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.CitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var selectJob: Job? = null

        override fun executeIntent(intent: Intent) = when(intent) {
            Intent.ClickAddCity -> publish(Label.ClickAddCity)
            Intent.ClickBack -> publish(Label.ClickBack)
            is Intent.SwipeRemoveCity ->  {
                selectJob?.cancel()
                selectJob = scope.launch {
                    Log.d("Xyi", "Removing city: ${intent.city.name}")
                    changeFavouriteStateUseCase.removeFromFavourite(intent.city.name)
                    publish(Label.SwipeRemoveCity(intent.city))
                }
            }
        }

        override fun executeAction(action: Action) = when(action) {
            is Action.CitiesLoaded -> {
                val cities = action.cities
                dispatch(Msg.CitiesLoaded(cities))
                Log.d("SelectCityStore", "Cities loaded: $cities")
                cities.forEach { city ->
                    scope.launch {
                        loadWeatherForCity(city)
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherLoading(city.name))
            try {
                val weather = loadWeatherForCityExplicitUseCase(city.lat, city.long)
                dispatch(
                    Msg.WeatherLoaded(
                        cityName = city.name,
                        temp = weather.current.tempC.toInt(),
                        description = weather.current.condition.text,
                        icon = weather.current.condition.code.findIconForCode()
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherError(city.name))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.CitiesLoaded -> {
                Log.d("SelectCityStore", "Reducer: CitiesLoaded")
                copy(
                    cityItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            cityItemState = State.CityItemState.Initial
                        )
                    }
                )
            }
            is Msg.WeatherError -> {
                Log.d("SelectCityStore", "Reducer: WeatherError for ${msg.cityName}")
                copy(
                    cityItems = cityItems.map {
                        if (it.city.name == msg.cityName) {
                            it.copy(cityItemState = State.CityItemState.Error)
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoaded -> {
                Log.d("SelectCityStore", "Reducer: WeatherLoaded for ${msg.cityName}")
                copy(
                    cityItems = cityItems.map {
                        if (it.city.name == msg.cityName) {
                            it.copy(cityItemState = State.CityItemState.Loaded(
                                cityName = msg.cityName,
                                temp = msg.temp,
                                description = msg.description,
                                icon = msg.icon
                            ))
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoading -> {
                Log.d("SelectCityStore", "Reducer: WeatherLoading for ${msg.cityName}")
                copy(
                    cityItems = cityItems.map {
                        if (it.city.name == msg.cityName) {
                            it.copy(cityItemState = State.CityItemState.Loading)
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}
