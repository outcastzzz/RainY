package com.example.rainy.presentation.root

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.domain.entity.Astronomy
import com.example.domain.entity.InfoData
import com.example.domain.entity.Weather
import com.example.rainy.presentation.forecast.ForecastComponentImpl
import com.example.rainy.presentation.main.MainComponentImpl
import com.example.rainy.presentation.searchCity.SearchCityComponentImpl
import com.example.rainy.presentation.selectCity.SelectCityComponentImpl
import com.example.rainy.presentation.settings.SettingsComponentImpl
import com.example.rainy.presentation.splash.SplashComponentImpl
import com.example.rainy.presentation.weather.WeatherComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class RootComponentImpl @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("lat") private val lat: Float,
    @Assisted("long") private val long: Float,
    private val weatherComponentFactory: WeatherComponentImpl.Factory,
    private val forecastComponentFactory: ForecastComponentImpl.Factory,
    private val mainComponentFactory: MainComponentImpl.Factory,
    private val settingsComponentFactory: SettingsComponentImpl.Factory,
    private val splashComponentFactory: SplashComponentImpl.Factory,
    private val selectComponentFactory: SelectCityComponentImpl.Factory,
    private val searchCityComponentFactory: SearchCityComponentImpl.Factory,
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.SplashConfig(
           lat, long
        ),
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when(config) {
            is Config.DetailsConfig -> {
                val component = weatherComponentFactory.create(
                    componentContext = componentContext,
                    weather = config.weather,
                    astronomy = config.astronomy
                )
                RootComponent.Child.Details(component)
            }
            is Config.ForecastConfig -> {
                val component = forecastComponentFactory.create(
                    componentContext = componentContext,
                    forecast = config.forecast
                )
                RootComponent.Child.Forecast(component)
            }
            is Config.MainConfig -> {
                val mainComponent = mainComponentFactory.create(
                    componentContext = componentContext,
                    onClickSelectCity = {
                        navigation.push(Config.SelectCityConfig)
                    },
                    onClickSettings = {
                        navigation.push(Config.SettingsConfig)
                    },
                    infoData = config.infoData
                )
                val weatherComponent = weatherComponentFactory.create(
                    componentContext = componentContext,
                    weather = config.infoData.weather,
                    astronomy = config.infoData.astronomy
                )
                val forecastComponent = forecastComponentFactory.create(
                    componentContext = componentContext,
                    forecast = config.infoData.weather
                )
                RootComponent.Child.Main(mainComponent, weatherComponent, forecastComponent)
            }
            is Config.WeatherConfig -> {
                val component = weatherComponentFactory.create(
                    componentContext = componentContext,
                    weather = config.weather,
                    astronomy = config.astronomy
                )
                RootComponent.Child.Weather(component)
            }
            Config.SettingsConfig -> {
                val component = settingsComponentFactory.create(
                    componentContext = componentContext,
                    onBackClicked = { navigation.pop() }
                )
                RootComponent.Child.Settings(component)
            }
            is Config.SplashConfig -> {
                val component = splashComponentFactory.create(
                    componentContext = componentContext,
                    lat = config.lat,
                    long = config.long,
                    onDataLoaded = {
                        navigation.push(
                            Config.MainConfig(it)
                        )
                    }
                )
                RootComponent.Child.Splash(component)
            }
            Config.SelectCityConfig -> {
                val component = selectComponentFactory.create(
                    componentContext = componentContext,
                    clickBack = { navigation.pop() },
                    clickAddCity = { navigation.push(Config.SearchCity) },
                    onSwipeRemoveCity = {
                        Log.d("asd", "asd")
                    }
                )
                RootComponent.Child.SelectCity(component)
            }
            Config.SearchCity -> {
                val component = searchCityComponentFactory.create(
                    componentContext = componentContext,
                    onBackClicked = { navigation.pop() },
                    onCitySavedToFavourite = { navigation.pop() }
                )
                RootComponent.Child.SearchCity(component)
            }
        }
    }

    @Serializable
    sealed class Config {

        @Serializable
        data class MainConfig(val infoData: InfoData): Config()

        @Serializable
        data class ForecastConfig(val forecast: Weather): Config()

        @Serializable
        data class WeatherConfig(val weather: Weather, val astronomy: Astronomy): Config()

        @Serializable
        data class DetailsConfig(val weather: Weather, val astronomy: Astronomy): Config()

        @Serializable
        data object SettingsConfig: Config()

        @Serializable
        data class SplashConfig(val lat: Float, val long: Float): Config()

        @Serializable
        data object SelectCityConfig: Config()

        @Serializable
        data object SearchCity: Config()

    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("lat") lat: Float,
            @Assisted("long") long: Float
        ): RootComponentImpl
    }

}