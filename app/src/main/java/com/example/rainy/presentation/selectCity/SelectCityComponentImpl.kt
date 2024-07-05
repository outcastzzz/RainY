package com.example.rainy.presentation.selectCity

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.domain.entity.City
import com.example.rainy.presentation.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectCityComponentImpl @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onClickBack") private val clickBack: () -> Unit,
    @Assisted("onClickAddCity") private val clickAddCity: () -> Unit,
    @Assisted("swipeRemoveCity") private val onSwipeRemoveCity: (City) -> Unit,
    private val selectCityStoreFactory: SelectCityStoreFactory
): SelectCityComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { selectCityStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    SelectCityStore.Label.ClickAddCity -> clickAddCity()
                    SelectCityStore.Label.ClickBack -> clickBack()
                    is SelectCityStore.Label.SwipeRemoveCity -> onSwipeRemoveCity(label.city)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SelectCityStore.State> = store.stateFlow

    override fun onClickBack() = store.accept(SelectCityStore.Intent.ClickBack)

    override fun onClickAddCity() = store.accept(SelectCityStore.Intent.ClickAddCity)

    override fun swipeRemoveCity(city: City) = store
        .accept(SelectCityStore.Intent.SwipeRemoveCity(city))


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickBack") clickBack: () -> Unit,
            @Assisted("onClickAddCity") clickAddCity: () -> Unit,
            @Assisted("swipeRemoveCity") onSwipeRemoveCity: (City) -> Unit,
        ): SelectCityComponentImpl
    }
}