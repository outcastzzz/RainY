package com.example.weather.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.ui.DetailsTextBlock
import com.example.weather.WeatherComponent

@Composable
fun DetailsContent(
    modifier: Modifier,
    component: WeatherComponent
) {

    val state by component.model.collectAsStateWithLifecycle()
    val weather = state.weather

    Scaffold (
        modifier = modifier
            .fillMaxSize(),
    ) { paddingValues ->

        Column (
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 30.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            Text(
                text = "Details",
                style = MaterialTheme.typography.displayLarge,
            )

            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                val precipitation = weather.current.precip.toString()
                val windVector = weather.current.windDir
                val windKph = weather.current.windKph
                val humidity = weather.current.humidity
                val visibility = weather.current.visibility
                val uv = weather.current.uv.toString()
                val pressure = weather.current.pressureMb.toInt()

                DetailsTextBlock("Precipitation", "$precipitation mm" )
                DetailsTextBlock("$windVector Wind", "$windKph km/h")
                DetailsTextBlock("Humidity", "$humidity %")
                DetailsTextBlock("Visibility", "$visibility km")
                DetailsTextBlock("UV", uv)
                DetailsTextBlock("Pressure", "$pressure hPa")

            }
        }

    }

}
