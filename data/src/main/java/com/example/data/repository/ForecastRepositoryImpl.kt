package com.example.data.repository

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.data.BuildConfig
import com.example.data.database.dao.WeatherDao
import com.example.data.mapper.fromWeatherDbo
import com.example.data.mapper.toWeatherDbo
import com.example.data.network.ApiRoutes
import com.example.data.workers.RefreshForecastDataWorker
import com.example.domain.entity.Astronomy
import com.example.domain.entity.Weather
import com.example.domain.repository.ForecastRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val weatherDao: WeatherDao,
    private val application: Application
): ForecastRepository {

    override suspend fun getCurrentCityName(lat: Float, long: Float): String {
        val response = client.get(ApiRoutes.SEARCH_ROUTE) {
            url {
                parameters.append("lat", lat.toString())
                parameters.append("lon", long.toString())
                parameters.append("appid", BuildConfig.WEATHER_KEY)
            }
            contentType(ContentType.Application.Json)
        }.body<List<com.example.domain.entity.City?>>()
        return response[0]?.name ?: "We don`t know where at you"
    }

    override suspend fun loadWeatherForCityImplicit(lat: Float, long: Float) {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            RefreshForecastDataWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            RefreshForecastDataWorker.makePeriodicRequest(lat, long)
        )
    }

    override suspend fun loadWeatherForCityExplicit(lat: Float, long: Float): Weather {
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

    override suspend fun getWeather(lat: Float, long: Float): Weather {
        return try {
            Log.d("worker", "working good")
            fromWeatherDbo(weatherDao.getWeather())
        } catch(e: Exception) {
            Log.d("worker", "working with Error")
            val requestParams = "$lat, $long"
            val weather = client.get(ApiRoutes.FORECAST) {
                url {
                    parameters.append("key", BuildConfig.APP_ID)
                    parameters.append("q", requestParams)
                    parameters.append("days", 3.toString())
                }
                contentType(ContentType.Application.Json)
            }.body<Weather>()
            weatherDao.insertWeather(toWeatherDbo(weather))
            return weather
        }

    }

}