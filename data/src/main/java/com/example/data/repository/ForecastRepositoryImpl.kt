package com.example.data.repository

import android.util.Log
import com.example.data.BuildConfig
import com.example.domain.repository.ForecastRepository
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
        val response = client.get(com.example.data.network.ApiRoutes.SEARCH_ROUTE) {
            url {
                parameters.append("lat", lat.toString())
                parameters.append("lon", long.toString())
                parameters.append("appid", BuildConfig.WEATHER_KEY)
            }
            contentType(ContentType.Application.Json)
        }.body<List<com.example.domain.entity.City?>>()
        Log.d("mytag", "$response")
        return response[0]?.name ?: "We don`t know where at you"
    }

    override suspend fun loadWeatherForCity(lat: Float, long: Float): com.example.domain.entity.Weather {
        val requestParams = "$lat, $long"
        return client.get(com.example.data.network.ApiRoutes.FORECAST) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
                parameters.append("days", 3.toString())
            }
            contentType(ContentType.Application.Json)
        }.body<com.example.domain.entity.Weather>()
    }

    override suspend fun loadAstronomyData(lat: Float, long: Float): com.example.domain.entity.Astronomy {
        val requestParams = "$lat, $long"
        return client.get(com.example.data.network.ApiRoutes.ASTRONOMY) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
            }
            contentType(ContentType.Application.Json)
        }.body<com.example.domain.entity.Astronomy>()
    }

    override suspend fun loadTodayHourlyForecast(lat: Float, long: Float): com.example.domain.entity.Forecast {
        val requestParams = "$lat, $long"
        return client.get(com.example.data.network.ApiRoutes.FORECAST) {
            url {
                parameters.append("key", BuildConfig.APP_ID)
                parameters.append("q", requestParams)
            }
            contentType(ContentType.Application.Json)
        }.body<com.example.domain.entity.Forecast>()
    }

}