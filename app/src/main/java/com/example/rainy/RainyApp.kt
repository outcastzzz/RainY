package com.example.rainy

import android.app.Application
import com.example.rainy.di.ApplicationComponent
import com.example.rainy.di.DaggerApplicationComponent

class RainyApp: Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }


}