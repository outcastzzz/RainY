package com.example.data.mapper

import com.example.data.database.dbo.AstroEntity
import com.example.data.database.dbo.ConditionEntity
import com.example.data.database.dbo.CurrentEntity
import com.example.data.database.dbo.DayEntity
import com.example.data.database.dbo.ForecastDayEntity
import com.example.data.database.dbo.ForecastEntity
import com.example.data.database.dbo.HourEntity
import com.example.data.database.dbo.LocationEntity
import com.example.data.database.dbo.WeatherDbo
import com.example.domain.entity.Astro
import com.example.domain.entity.Condition
import com.example.domain.entity.Current
import com.example.domain.entity.Day
import com.example.domain.entity.Forecast
import com.example.domain.entity.ForecastDay
import com.example.domain.entity.Hour
import com.example.domain.entity.Location
import com.example.domain.entity.Weather


fun toWeatherDbo(weather: Weather): WeatherDbo = WeatherDbo(
    location = toLocationEntity(weather.location),
    current = toCurrentEntity(weather.current),
    forecast = toForecastEntity(weather.forecast)
)

fun fromWeatherDbo(weatherEntity: WeatherDbo): Weather = Weather(
    location = fromLocationEntity(weatherEntity.location),
    current = fromCurrentEntity(weatherEntity.current),
    forecast = fromForecastEntity(weatherEntity.forecast)
)

private fun toLocationEntity(location: Location): LocationEntity = LocationEntity(
    name = location.name,
    region = location.region,
    country = location.country,
    epoch = location.epoch
)


private fun fromLocationEntity(locationEntity: LocationEntity): Location = Location(
    name = locationEntity.name,
    region = locationEntity.region,
    country = locationEntity.country,
    epoch = locationEntity.epoch
)

private fun toCurrentEntity(current: Current): CurrentEntity = CurrentEntity(
    lastUpdated = current.lastUpdate,
    tempC = current.tempC,
    condition = toConditionEntity(current.condition),
    windKph = current.windKph,
    windDir = current.windDir,
    pressureMb = current.pressureMb,
    precipMm = current.precip,
    humidity = current.humidity,
    cloud = current.cloud,
    feelslike = current.feelsLike,
    visibility = current.visibility,
    uv = current.uv
)


private fun fromCurrentEntity(currentEntity: CurrentEntity): Current = Current(
    lastUpdate = currentEntity.lastUpdated,
    tempC = currentEntity.tempC,
    condition = fromConditionEntity(currentEntity.condition),
    windKph = currentEntity.windKph,
    windDir = currentEntity.windDir,
    pressureMb = currentEntity.pressureMb,
    precip = currentEntity.precipMm,
    humidity = currentEntity.humidity,
    cloud = currentEntity.cloud,
    feelsLike = currentEntity.feelslike,
    visibility = currentEntity.visibility,
    uv = currentEntity.uv
)

private fun toConditionEntity(condition: Condition): ConditionEntity {
    return ConditionEntity(
        text = condition.text,
        code = condition.code
    )
}

private fun fromConditionEntity(conditionEntity: ConditionEntity): Condition {
    return Condition(
        text = conditionEntity.text,
        code = conditionEntity.code
    )
}

private fun toForecastEntity(forecast: Forecast): ForecastEntity {
    return ForecastEntity(
        forecastday = forecast.forecastDay.map { toForecastDayEntity(it) }
    )
}

private fun fromForecastEntity(forecastEntity: ForecastEntity): Forecast {
    return Forecast(
        forecastDay = forecastEntity.forecastday.map { fromForecastDayEntity(it) }
    )
}

private fun toForecastDayEntity(forecastDay: ForecastDay): ForecastDayEntity {
    return ForecastDayEntity(
        date = forecastDay.date,
        day = toDayEntity(forecastDay.day),
        astro = toAstroEntity(forecastDay.astro),
        hour = forecastDay.hour.map { toHourEntity(it) }
    )
}

private fun fromForecastDayEntity(forecastDayEntity: ForecastDayEntity): ForecastDay {
    return ForecastDay(
        date = forecastDayEntity.date,
        day = fromDayEntity(forecastDayEntity.day),
        astro = fromAstroEntity(forecastDayEntity.astro),
        hour = forecastDayEntity.hour.map { fromHourEntity(it) }
    )
}

private fun toDayEntity(day: Day): DayEntity {
    return DayEntity(
        maxtemp = day.maxTemp,
        mintemp = day.minTemp,
        avgtemp = day.avgTemp,
        totalPrecip = day.totalPrecip,
        avgVisibility = day.avgVisibility,
        avghumidity = day.avgHumidity,
        rainChance = day.rainChance,
        condition = toConditionEntity(day.condition),
        uv = day.uv
    )
}

private fun fromDayEntity(dayEntity: DayEntity): Day {
    return Day(
        maxTemp = dayEntity.maxtemp,
        minTemp = dayEntity.mintemp,
        avgTemp = dayEntity.avgtemp,
        totalPrecip = dayEntity.totalPrecip,
        avgVisibility = dayEntity.avgVisibility,
        avgHumidity = dayEntity.avghumidity,
        rainChance = dayEntity.rainChance,
        condition = fromConditionEntity(dayEntity.condition),
        uv = dayEntity.uv
    )
}

private fun toHourEntity(hour: Hour): HourEntity {
    return HourEntity(
        time = hour.time,
        temp = hour.temp,
        condition = toConditionEntity(hour.condition)
    )
}

private fun fromHourEntity(hourEntity: HourEntity): Hour {
    return Hour(
        time = hourEntity.time,
        temp = hourEntity.temp,
        condition = fromConditionEntity(hourEntity.condition)
    )
}

private fun toAstroEntity(astro: Astro): AstroEntity {
    return AstroEntity(
        sunrise = astro.sunrise,
        sunset = astro.sunset,
        moonrise = astro.moonrise,
        moonset = astro.moonset,
        moonPhase = astro.moonPhase
    )
}

private fun fromAstroEntity(astroEntity: AstroEntity): Astro {
    return Astro(
        sunrise = astroEntity.sunrise,
        sunset = astroEntity.sunset,
        moonrise = astroEntity.moonrise,
        moonset = astroEntity.moonset,
        moonPhase = astroEntity.moonPhase
    )
}

