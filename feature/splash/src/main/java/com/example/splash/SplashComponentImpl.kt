package com.example.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.common.componentScope
import com.example.domain.entity.InfoData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashComponentImpl @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("lat") private val lat: Float,
    @Assisted("long") private val long: Float,
    @Assisted("onDataLoaded") private val onDataLoaded: (InfoData) -> Unit,
    private val splashStoreFactory: SplashStoreFactory
): SplashComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { splashStoreFactory.create(lat, long, stateKeeper) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    is SplashStore.Label.DataLoaded -> onDataLoaded(
                        label.data
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SplashStore.State> = store.stateFlow

    override fun dataLoaded(
        data: InfoData
    ) {
        store.accept(SplashStore.Intent.DataLoaded(data))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("lat") lat: Float,
            @Assisted("long") long: Float,
            @Assisted("onDataLoaded") onDataLoaded: (InfoData) -> Unit,
        ): SplashComponentImpl
    }

}