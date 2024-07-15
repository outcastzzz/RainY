package com.example.rainy.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.common.theme.RainYTheme
import com.example.main.MainContent
import com.example.searchcity.SearchContent
import com.example.selectcity.SelectCityContent
import com.example.settings.SettingsContent
import com.example.splash.SplashContent
import com.example.weather.details.DetailsContent

@Composable
fun RootContent(component: RootComponent) {

    RainYTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }
                is RootComponent.Child.Main -> MainContent(
                    mainComponent = instance.mainComponent,
                    weatherComponent = instance.weatherComponent,
                    forecastComponent = instance.forecastComponent
                )
                is RootComponent.Child.Settings -> SettingsContent(instance.component)
                is RootComponent.Child.Splash -> SplashContent(instance.component)
                is RootComponent.Child.SelectCity -> SelectCityContent(instance.component)
                is RootComponent.Child.SearchCity -> SearchContent(instance.component)
                is RootComponent.Child.Forecast -> {}
                is RootComponent.Child.Weather -> {}
            }
        }
    }

}