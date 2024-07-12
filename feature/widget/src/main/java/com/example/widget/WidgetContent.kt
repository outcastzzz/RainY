package com.example.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun WidgetContent(
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .cornerRadius(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {

//        val icon = weather.forecast.forecastDay[0].day.condition.code.findIconForCode()
//        val avgTemp = weather.forecast.forecastDay[0].day.avgTemp
//        val maxTemp = weather.forecast.forecastDay[0].day.maxTemp
//        val minTemp = weather.forecast.forecastDay[0].day.minTemp
        val icon = com.example.common.R.drawable.ic_sunny
        val avgTemp = 24
        val maxTemp = 32
        val minTemp = 19

        val fontMedium = TextStyle(
            color = ColorProvider(Color.White),
        )

        val fontSmall = TextStyle(
            color = ColorProvider(Color.White),
        )

        Column(
            modifier = modifier
                .background(Color.Black)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier
                    .size(24.dp),
                provider = ImageProvider(icon),
                contentDescription = "widget icon"
            )
            Spacer(modifier.height(5.dp))
            Text(
                modifier = modifier,
                text = avgTemp.toString(),
                style = fontMedium,
            )
            Row(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = modifier,
                    text = maxTemp.toString(),
                    style = fontSmall
                )
                Spacer(modifier.width(5.dp))
                Text(
                    modifier = modifier,
                    text = minTemp.toString(),
                    style = fontSmall
                )
            }
        }
    }
}

