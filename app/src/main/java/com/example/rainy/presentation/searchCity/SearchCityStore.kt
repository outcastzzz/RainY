package com.example.rainy.presentation.searchCity

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.rainy.presentation.searchCity.SearchCityStore.Intent
import com.example.rainy.presentation.searchCity.SearchCityStore.Label
import com.example.rainy.presentation.searchCity.SearchCityStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchCityStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeSearchQuery(val query: String) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent

        data class ClickCity(val city: com.example.domain.entity.City) : Intent

    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {

            data object Initial: SearchState

            data object Loading: SearchState

            data object Error: SearchState

            data object EmptyResult: SearchState

            data class Loaded(val cities: List<com.example.domain.entity.City>): SearchState

        }

    }

    sealed interface Label {

        data object ClickBack : Label

        data object SavedToFavourite : Label

    }
}

class SearchCityStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeFavouriteStateUseCase: com.example.domain.useCase.ChangeFavouriteStateUseCase,
    private val searchCityUseCase: com.example.domain.useCase.SearchCityUseCase
) {

    fun create(): SearchCityStore =
        object : SearchCityStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchCityStore",
            initialState = State(
                "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg

        data object LoadingSearchResult : Msg

        data object SearchResultError : Msg

        data class SearchResultLoaded(val cities: List<com.example.domain.entity.City>) : Msg

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent) = when(intent) {
            is Intent.ChangeSearchQuery -> dispatch(Msg.ChangeSearchQuery(intent.query))
            Intent.ClickBack -> publish(Label.ClickBack)
            is Intent.ClickCity -> {
                searchJob?.cancel()
                searchJob = scope.launch {
                    changeFavouriteStateUseCase.addToFavourite(intent.city)
                    publish(Label.SavedToFavourite)
                }
            }
            Intent.ClickSearch -> {
                searchJob?.cancel()
                searchJob = scope.launch {
                    dispatch(Msg.LoadingSearchResult)
                    try {
                        val cities = searchCityUseCase(state().searchQuery)
                        dispatch(Msg.SearchResultLoaded(cities))
                    } catch (e: Exception) {
                        dispatch(Msg.SearchResultError)
                    }
                }
            }
        }

        override fun executeAction(action: Action) {}
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.ChangeSearchQuery -> copy(searchQuery = msg.query)
            Msg.LoadingSearchResult -> copy(searchState = State.SearchState.Loading)
            Msg.SearchResultError -> copy(searchState = State.SearchState.Error)
            is Msg.SearchResultLoaded -> {
                val searchState = if(msg.cities.isEmpty()) {
                    State.SearchState.EmptyResult
                } else {
                    State.SearchState.Loaded(msg.cities)
                }
                copy(searchState = searchState)
            }
        }
    }
}
