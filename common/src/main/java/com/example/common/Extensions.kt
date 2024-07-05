package com.example.common

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val mainDateTimeFormat by lazy {
    SimpleDateFormat("EEE, d MMM yyyy", Locale.US)
}

private val sunsetTimeFormat by lazy {
    SimpleDateFormat("HH:mm", Locale.getDefault())
}

private val dailyForecastTimeFormat by lazy {
    SimpleDateFormat("d MMM", Locale.US)
}

private val forecastTimeFormat by lazy {
    SimpleDateFormat("HH:mm", Locale.getDefault())
}

fun ComponentContext.componentScope() = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

fun Long.toSmallDate(): String {
    return dailyForecastTimeFormat.format(Date(this * 1000))
}

fun Long.toDateString(): String {
    return mainDateTimeFormat.format(Date(this * 1000))
}

fun String.toTimeString(): String {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(this)
    return sunsetTimeFormat.format(date!!)
}

fun Long.toTimeFormat(): String {
    return forecastTimeFormat.format(this * 1000)
}

fun Int.findIconForCode(): Int {
    return when(this) {
        1000 -> R.drawable.ic_sunny
        1003 -> R.drawable.ic_partly_cloudy
        1006 -> R.drawable.ic_cloudy
        1009 -> R.drawable.ic_overcast
        1030 -> R.drawable.ic_mist
        1063 -> R.drawable.ic_patchy_rain
        1066 -> R.drawable.ic_patchy_snow
        1069 -> R.drawable.ic_patchy_sleet
        1072 -> R.drawable.ic_freezing_drizzle
        1087 -> R.drawable.ic_thundery_outbreaks
        1114 -> R.drawable.ic_blowing_snow
        1017 -> R.drawable.ic_blizzard
        1135 -> R.drawable.ic_mist
        1147 -> R.drawable.ic_freezing_fog
        1150 -> R.drawable.ic_patchy_light_drizzle
        1153 -> R.drawable.ic_patchy_light_drizzle
        1168 -> R.drawable.ic_freezing_drizzle
        1171 -> R.drawable.ic_heavy_freezing_drizzle
        1180 -> R.drawable.ic_patchy_light_rain
        1183 -> R.drawable.ic_light_rain
        1186 -> R.drawable.ic_light_rain
        1189 -> R.drawable.ic_light_rain
        1192 -> R.drawable.ic_heavy_rain
        1195 -> R.drawable.ic_heavy_rain
        1198 -> R.drawable.ic_heavy_freezing_drizzle
        1210 -> R.drawable.ic_patchy_light_snow
        1213 -> R.drawable.ic_patchy_snow
        1219 -> R.drawable.ic_moderate_snow
        1222 -> R.drawable.ic_patchy_heavy_snow
        1225 -> R.drawable.ic_heavy_snow
        1237 -> R.drawable.ic_heavy_ice_pellets
        else -> R.drawable.ic_unknown
    }
}