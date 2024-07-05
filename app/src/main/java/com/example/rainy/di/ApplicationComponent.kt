package com.example.rainy.di

import android.content.Context
import com.example.rainy.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import di.DataComponent
import di.DataModule

@[
    ApplicationScope
    Component(
        modules = [
            PresentationModule::class,
            DataModule::class
        ]
    )
]
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun dataComponent(): DataComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}