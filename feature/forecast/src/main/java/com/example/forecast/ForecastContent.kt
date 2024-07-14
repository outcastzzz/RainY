package com.example.forecast

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.R
import com.example.common.theme.TextColorAccent
import com.example.common.utils.findIconForCode
import com.example.common.utils.toSmallDate
import com.example.common.utils.toTimeFormat
import com.example.domain.entity.ForecastDay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ForecastContent(component: ForecastComponent) {

    val state by component.model.collectAsStateWithLifecycle()
    val hourlyItems = state.hourlyForecast
    val dailyItems = state.dailyForecast.forecastDay

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) { paddingValues ->

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val currentTime = System.currentTimeMillis()
        val currentHour = Calendar.getInstance().apply {
            timeInMillis = currentTime
        }.get(Calendar.HOUR_OF_DAY)

        val initialIndex = hourlyItems.indexOfFirst { hour ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = hour.time * 1000
            }
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            hourOfDay == currentHour
        }.takeIf { it >= 0 } ?: 0

        val density = LocalDensity.current
        val offsetPx = with(density) { 30.dp.toPx() }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                listState.scrollToItem(initialIndex)
                listState.scrollBy(-offsetPx)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                ),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            Text(
                modifier = Modifier
                    .padding(start = 30.dp),
                text = "Forecast",
                style = MaterialTheme.typography.displayLarge
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 30.dp),
                    text = "Hourly Forecast",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextColorAccent
                )
                Spacer(Modifier.height(20.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    itemsIndexed(hourlyItems) { index, hour ->
                        val paddingModifier = when (index) {
                            0 -> Modifier.padding(start = 30.dp)
                            hourlyItems.size - 1 -> Modifier.padding(end = 30.dp)
                            else -> Modifier
                        }
                        HourlyItem(
                            hour.time,
                            hour.condition.code.findIconForCode(),
                            paddingModifier
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 30.dp),
                    text = "Daily Forecast",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextColorAccent
                )
                Spacer(Modifier.height(20.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    itemsIndexed(dailyItems) { index, day ->
                        val paddingModifier = when (index) {
                            0 -> Modifier.padding(start = 30.dp)
                            dailyItems.size - 1 -> Modifier.padding(end = 30.dp)
                            else -> Modifier
                        }
                        DailyItem(
                            day,
                            day.day.condition.code.findIconForCode(),
                            paddingModifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HourlyItem(hour: Long, icon: Int, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hour.toTimeFormat(),
            style = MaterialTheme.typography.displaySmall,
            color = TextColorAccent
        )
        Spacer(Modifier.height(10.dp))
        Image(
            painter = painterResource(icon),
            contentDescription = "hourly forecast icon",
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
private fun DailyItem(
    day: ForecastDay,
    icon: Int,
    modifier: Modifier = Modifier
) {

    val minTemp = day.day.minTemp.roundToInt().toString()
    val maxTemp = day.day.maxTemp.roundToInt().toString()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.date.toSmallDate(),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Image(
            painter = painterResource(icon),
            contentDescription = "daily forecast icon",
            modifier = Modifier
                .size(24.dp)
        )
        Row {
            Image(
                painter = painterResource(R.drawable.ic_max_temp),
                contentDescription = "daily max temp",
                modifier = Modifier
                    .size(11.dp)
            )
            Text(
                text = "$maxTemp°C",
                style = MaterialTheme.typography.titleSmall,
                color = TextColorAccent
            )
        }
        Row {
            Image(
                painter = painterResource(R.drawable.ic_min_temp),
                contentDescription = "daily max temp",
                modifier = Modifier
                    .size(11.dp)
            )
            Text(
                text = "$minTemp°C",
                style = MaterialTheme.typography.titleSmall,
                color = TextColorAccent
            )
        }
    }
}