package com.example.main

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.R
import com.example.common.utils.BackHandler
import com.example.forecast.ForecastComponent
import com.example.forecast.ForecastContent
import com.example.weather.WeatherComponent
import com.example.weather.WeatherContent
import com.example.weather.details.DetailsContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    modifier: Modifier,
    mainComponent: MainComponent,
    weatherComponent: WeatherComponent,
    forecastComponent: ForecastComponent
) {

    val state by mainComponent.model.collectAsStateWithLifecycle()
    val cityName = state.data.cityName

    val pagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 1
    )

    val activity = (LocalContext.current as? Activity)

    BackHandler(backHandler = mainComponent.backHandler) {
        activity?.finish()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        topBar = {
            MainTopBar(
                cityName = cityName,
                onClickSettings = { mainComponent.onClickSettings() }
            ) { mainComponent.onClickSelectCity() }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary),
        ) { page ->
            when (page) {
                0 -> DetailsContent(modifier, weatherComponent)
                1 -> WeatherContent(modifier, weatherComponent)
                2 -> ForecastContent(modifier, forecastComponent)
            }
        }
    }

}

@Composable
private fun MainTopBar(
    modifier: Modifier = Modifier,
    cityName: String,
    onClickSettings: () -> Unit,
    onClickCities: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                top = 70.dp,
                start = 30.dp,
                end = 30.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentCityText(cityName)
        Spacer(modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.ic_map),
            contentDescription = "select city",
            modifier = modifier
                .clickable {
                    onClickCities()
                }
        )
        Spacer(modifier.width(20.dp))
        IconButton(onClick = { onClickSettings() }) {
            Image(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "settings",
            )
        }
    }
}

@Composable
private fun CurrentCityText(
    cityName: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = cityName,
        style = MaterialTheme.typography.displayMedium,
        modifier = modifier
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.secondary
    )
}
