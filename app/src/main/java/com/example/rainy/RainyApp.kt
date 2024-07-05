package com.example.rainy

import android.app.Application
import com.example.rainy.di.ApplicationComponent
import com.example.rainy.di.DaggerApplicationComponent
import di.DataComponent
import di.DataComponentProvider

class RainyApp: Application(), DataComponentProvider {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }

    override fun provideDataComponent(): DataComponent {
        return applicationComponent.dataComponent().create()
    }

}