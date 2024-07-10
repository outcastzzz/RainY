package com.example.rainy

import android.app.Application
import androidx.work.Configuration
import com.example.data.workers.ForecastWorkerFactory
import com.example.rainy.di.ApplicationComponent
import com.example.rainy.di.DaggerApplicationComponent
import javax.inject.Inject

class RainyApp: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: ForecastWorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}