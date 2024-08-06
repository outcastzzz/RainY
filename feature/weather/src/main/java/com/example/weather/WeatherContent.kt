package com.example.weather

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.R
import com.example.common.theme.displayFontFamily
import com.example.common.utils.findIconForCode
import com.example.common.utils.toDateString
import com.example.common.utils.toTimeString
import com.example.domain.entity.Hour
import kotlin.math.roundToInt

@Composable
fun WeatherContent(
    modifier: Modifier,
    component: WeatherComponent
) {

    val state by component.model.collectAsStateWithLifecycle()
    val weather = state.weather
    val astro = state.astronomy

    Scaffold (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 30.dp),
    ) { paddingValues ->

        val hourlyTemp = weather.forecast.forecastDay[0].hour

        fun findCurrentHourTemperature(hours: List<Hour>): String {

            val currentTime = System.currentTimeMillis()
            val currentHour = Calendar.getInstance().apply {
                timeInMillis = currentTime
            }.get(Calendar.HOUR_OF_DAY)

            val initialIndex = hours.indexOfFirst { hour ->
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = hour.time * 1000
                }
                val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
                hourOfDay == currentHour
            }.takeIf { it >= 0 } ?: 0

            return hours.getOrNull(initialIndex)?.temp?.roundToInt().toString()
        }

        Column (
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = weather.current.lastUpdate.toDateString(),
                    style = MaterialTheme.typography.displayMedium
                )
                Row (
                    modifier = modifier
                        .padding(top = 20.dp),
                ) {
                    Text(
                        text = findCurrentHourTemperature(hourlyTemp),
                        fontSize = 96.sp,
                        fontFamily = displayFontFamily
                    )
                    Text(
                        text = "°C",
                        fontSize = 32.sp,
                        fontFamily = displayFontFamily,
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                Row(
                    modifier = modifier
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_max_temp),
                            contentDescription = "min temp"
                        )
                        val maxTempStr = weather.forecast.forecastDay[0].day.maxTemp
                            .roundToInt()
                            .toString()
                        Text(
                            text = "$maxTempStr°C",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    Spacer(modifier.width(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_min_temp),
                            contentDescription = "min temp"
                        )
                        val minTempStr = weather.forecast.forecastDay[0].day.minTemp
                            .roundToInt()
                            .toString()
                        Text(
                            text = "$minTempStr°C",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(weather.current.condition.code.findIconForCode()),
                    contentDescription = "weather_image",
                    modifier = modifier
                        .size(128.dp)
                )
                Spacer(modifier.height(20.dp))
                Text(
                    text = weather.current.condition.text,
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Row(
                modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_sunrise),
                        contentDescription = "sunrise icon"
                    )
                    Spacer(modifier.width(10.dp))
                    Text(
                        text = astro.astronomy.astro.sunrise.toTimeString(),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                Spacer(modifier.width(30.dp))
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_sunset),
                        contentDescription = "sunrise icon"
                    )
                    Spacer(modifier.width(10.dp))
                    Text(
                        text = astro.astronomy.astro.sunset.toTimeString(),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }
    }
}

