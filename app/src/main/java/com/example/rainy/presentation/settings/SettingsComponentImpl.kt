package com.example.rainy.presentation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.example.rainy.presentation.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SettingsComponentImpl @AssistedInject constructor(
    private val storeFactory: SettingsStoreFactory,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
): SettingsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect { label ->
                when(label) {
                    SettingsStore.Label.ClickBack -> onBackClicked()
                }
            }
        }
    }

    override fun onClickBack() {
        store.accept(SettingsStore.Intent.ClickBack)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): SettingsComponentImpl
    }
}