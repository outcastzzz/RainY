package com.example.rainy.presentation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.domain.entity.InfoData
import com.example.rainy.presentation.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainComponentImpl @AssistedInject constructor(
    private val storeFactory: MainStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onClickSelectCity") private val clickSelectCity: () -> Unit,
    @Assisted("onClickSettings") private val clickSettings: () -> Unit,
    @Assisted("infoData") private val infoData: com.example.domain.entity.InfoData,
): MainComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(infoData, stateKeeper)
    }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    MainStore.Label.ClickSelectCity -> clickSelectCity()
                    MainStore.Label.ClickSettings -> clickSettings()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MainStore.State> = store.stateFlow

    override fun onClickSelectCity() = store.accept(MainStore.Intent.ClickSelectCity)

    override fun onClickSettings() = store.accept(MainStore.Intent.ClickSettings)

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickSelectCity") onClickSelectCity: () -> Unit,
            @Assisted("onClickSettings") onClickSettings: () -> Unit,
            @Assisted("infoData") infoData: com.example.domain.entity.InfoData,
        ): MainComponentImpl

    }

}