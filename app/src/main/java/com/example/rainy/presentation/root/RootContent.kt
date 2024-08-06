package com.example.rainy.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.common.theme.RainYTheme
import com.example.main.MainContent
import com.example.searchcity.SearchContent
import com.example.selectcity.SelectCityContent
import com.example.settings.SettingsContent
import com.example.splash.SplashContent
import com.example.weather.details.DetailsContent

@Composable
fun RootContent(modifier: Modifier = Modifier, component: RootComponent) {

    RainYTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(modifier, instance.component)
                }
                is RootComponent.Child.Main -> MainContent(
                    modifier,
                    mainComponent = instance.mainComponent,
                    weatherComponent = instance.weatherComponent,
                    forecastComponent = instance.forecastComponent
                )
                is RootComponent.Child.Settings -> SettingsContent(modifier, instance.component)
                is RootComponent.Child.Splash -> SplashContent(modifier, instance.component)
                is RootComponent.Child.SelectCity -> SelectCityContent(modifier, instance.component)
                is RootComponent.Child.SearchCity -> SearchContent(modifier, instance.component)
                is RootComponent.Child.Forecast -> {}
                is RootComponent.Child.Weather -> {}
            }
        }
    }

}