package com.example.rainy.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.rainy.presentation.forecast.ForecastComponent
import com.example.rainy.presentation.main.MainComponent
import com.example.rainy.presentation.searchCity.SearchCityComponent
import com.example.rainy.presentation.selectCity.SelectCityComponent
import com.example.rainy.presentation.settings.SettingsComponent
import com.example.rainy.presentation.splash.SplashComponent
import com.example.rainy.presentation.weather.WeatherComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Weather(val component: WeatherComponent): Child

        data class Details(val component: WeatherComponent): Child

        data class Forecast(val component: ForecastComponent): Child

        data class Settings(val component: SettingsComponent): Child

        data class Splash(val component: SplashComponent): Child

        data class SelectCity(val component: SelectCityComponent): Child

        data class SearchCity(val component: SearchCityComponent): Child

        data class Main(
            val mainComponent: MainComponent,
            val weatherComponent: WeatherComponent,
            val forecastComponent: ForecastComponent
        ): Child

    }

}