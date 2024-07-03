package com.example.rainy.presentation.details

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rainy.presentation.weather.WeatherComponent

@Composable
fun DetailsContent(component: WeatherComponent) {

    val state by component.model.collectAsStateWithLifecycle()
    val weather = state.weather

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->

        Column (
            modifier = Modifier
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
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Column {
                    Text(
                        "Precipitation",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.precip} mm",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Column {
                    val windVector = state.weather.current.windDir
                    Text(
                        "$windVector Wind",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.windKph} km/h",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Column {
                    Text(
                        "Humidity",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.humidity} %",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Column {
                    Text(
                        "Visibility",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.visibility} km",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Column {
                    Text(
                        "UV",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.uv}",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Column {
                    Text(
                        "Pressure",
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = "${weather.current.pressureMb.toInt()} hPa",
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

            }
        }

    }

}
