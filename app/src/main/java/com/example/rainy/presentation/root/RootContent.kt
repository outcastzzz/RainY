package com.example.rainy.presentation.root

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.rainy.presentation.details.DetailsContent
import com.example.rainy.presentation.main.MainContent
import com.example.rainy.presentation.searchCity.SearchContent
import com.example.rainy.presentation.selectCity.SelectCityContent
import com.example.rainy.presentation.settings.SettingsContent
import com.example.rainy.presentation.splash.SplashContent
import com.example.rainy.presentation.theme.RainYTheme
import com.example.rainy.presentation.weather.WeatherContent

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RootContent(component: RootComponent) {

    RainYTheme {
        Children(
            stack = component.stack
        ) {
            when(val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }
                is RootComponent.Child.Forecast -> {

                }
                is RootComponent.Child.Main -> {
                    MainContent(
                        mainComponent = instance.mainComponent,
                        weatherComponent = instance.weatherComponent,
                        forecastComponent = instance.forecastComponent
                    )
                }
                is RootComponent.Child.Weather -> {
                    WeatherContent(component = instance.component)
                }
                is RootComponent.Child.Settings -> SettingsContent(instance.component)
                is RootComponent.Child.Splash -> SplashContent(instance.component)
                is RootComponent.Child.SelectCity -> SelectCityContent(instance.component)
                is RootComponent.Child.SearchCity -> SearchContent(instance.component)
            }
        }
    }

}