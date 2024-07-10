package com.example.rainy.di

import android.app.Application
import com.example.rainy.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import di.DataModule
import di.WorkerModule

@[
    ApplicationScope
    Component(
        modules = [
            PresentationModule::class,
            DataModule::class,
            WorkerModule::class
        ]
    )
]
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): ApplicationComponent
    }

}