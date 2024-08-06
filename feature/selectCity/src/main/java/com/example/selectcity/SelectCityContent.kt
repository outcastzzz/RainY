package com.example.selectcity

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.R
import com.example.common.theme.TextColorAccent
import com.example.common.utils.findIconForCode
import com.example.domain.entity.City

@Composable
fun SelectCityContent(
    modifier: Modifier,
    component: SelectCityComponent
) {

    val state by component.model.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        topBar = {
            SelectCityTopBar(
                onClickAddCity = { component.onClickAddCity() }
            ) { component.onClickBack() }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.cityItems) { item ->
                CityItem(cityItem = item, city = item.city) { component.swipeRemoveCity(item.city) }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CityItem(
    modifier: Modifier = Modifier,
    cityItem: SelectCityStore.State.CityItem,
    city: City,
    onSwipe: (City) -> Unit,
) {
    val context = LocalContext.current

    when (val weather = cityItem.cityItemState) {
        SelectCityStore.State.CityItemState.Error -> SelectCityError()
        SelectCityStore.State.CityItemState.Initial -> {}
        is SelectCityStore.State.CityItemState.Loaded -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = 25.dp,
                        end = 25.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                onSwipe(city)
                                vibrateOnLongPress(context)
                            },
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = weather.cityName.split(',')[0],
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "${weather.temp}°C",
                            style = MaterialTheme.typography.displayMedium,
                            color = TextColorAccent
                        )
                        Text(
                            text = weather.description,
                            style = MaterialTheme.typography.displaySmall,
                            color = TextColorAccent
                        )
                    }
                    Spacer(modifier.weight(1f))
                    Image(
                        painter = painterResource(weather.icon.findIconForCode()),
                        contentDescription = "weather icon",
                        modifier = modifier
                            .size(40.dp)
                    )
                }
            }
        }
        SelectCityStore.State.CityItemState.Loading -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = 25.dp,
                        end = 25.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = MaterialTheme.shapes.large
            ) {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "",
                                style = MaterialTheme.typography.displayLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "",
                                style = MaterialTheme.typography.displayMedium,
                                color = TextColorAccent
                            )
                            Text(
                                text = "",
                                style = MaterialTheme.typography.displaySmall,
                                color = TextColorAccent
                            )
                        }
                    }
                    CircularProgressIndicator(
                        modifier = modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }

}

@Composable
private fun SelectCityTopBar(
    modifier: Modifier = Modifier,
    onClickAddCity: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 70.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() }) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    modifier = modifier
                        .padding(
                            start = 7.88.dp,
                            end = 7.88.dp,
                            top = 5.25.dp,
                            bottom = 5.25.dp
                        )
                        .clickable {
                            onBackClick()
                        },
                    contentDescription = "go back"
                )
            }
            Spacer(modifier.width(5.dp))
            Text(
                text = "Select City",
                fontSize = 18.sp,
                color = TextColorAccent
            )
            Spacer(modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_add_location),
                modifier = modifier
                    .clickable {
                        onClickAddCity()
                    },
                contentDescription = "add location"
            )
        }
    }

}

@Composable
private fun SelectCityError(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextColorAccent
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.displaySmall,
                    color = TextColorAccent
                )
            }
        }
        Image(
            modifier = modifier
                .fillMaxWidth()
                .size(36.dp),
            painter = painterResource(R.drawable.ic_unknown),
            contentDescription = "unable to load"
        )
    }
}

fun vibrateOnLongPress(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(90, 185))
    } else {
        vibrator.vibrate(200)
    }
}