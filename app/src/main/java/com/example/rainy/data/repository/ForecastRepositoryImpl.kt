package com.example.rainy.data.repository

import android.util.Log
import com.example.rainy.BuildConfig
import com.example.rainy.data.network.ApiRoutes
import com.example.rainy.domain.entity.Astronomy
import com.example.rainy.domain.entity.City
import com.example.rainy.domain.entity.Forecast
import com.example.rainy.domain.entity.Weather
import com.example.rainy.domain.repository.ForecastRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val client: HttpClient,
): ForecastRepository {

    override suspend fun getCurrentCityName(lat: Float, long: Float): String {
        val response = client.get(ApiRoutes.SEARCH_ROUTE) {
            url {
                parameters.append("lat", lat.toString())
                parameters.append("lon", long.toString())
                parameters.append("appid", BuildConfig.WEATHER_KEY)
            }
            contentType(ContentType.Application.Json)
        }.body<List<City?>>()
        Log.d("mytag", "$response")
        return response[0]?.name ?: "We don`t know where at you"
    }

    override suspend fun loadWeatherForCity(lat: Float, long: Float): Weather {
        val requestParams = "$lat, $long"
        return client.get(ApiRoutes.FORECAST) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
                parameters.append("days", 3.toString())
            }
            contentType(ContentType.Application.Json)
        }.body<Weather>()
    }

    override suspend fun loadAstronomyData(lat: Float, long: Float): Astronomy {
        val requestParams = "$lat, $long"
        return client.get(ApiRoutes.ASTRONOMY) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
            }
            contentType(ContentType.Application.Json)
        }.body<Astronomy>()
    }

    override suspend fun loadTodayHourlyForecast(lat: Float, long: Float): Forecast {
        val requestParams = "$lat, $long"
        return client.get(ApiRoutes.FORECAST) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
            }
            contentType(ContentType.Application.Json)
        }.body<Forecast>()
    }

}