package com.example.data.workers

import android.content.Context
import android.icu.util.Calendar
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.data.BuildConfig
import com.example.data.database.dao.WeatherDao
import com.example.data.mapper.toWeatherDbo
import com.example.data.network.ApiRoutes
import com.example.domain.entity.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshForecastDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val weatherDao: WeatherDao,
    private val client: HttpClient,
): CoroutineWorker(context, workerParameters) {

    private val mutex = Mutex()

    override suspend fun doWork(): Result {
        val lat = inputData.getFloat("lat", 0.0f)
        val long = inputData.getFloat("long", 0.0f)
        val requestParams = "$lat, $long"
        return mutex.withLock {
            val isEmpty = when (weatherDao.isEmpty()) {
                0 -> true
                else -> false
            }

            try {
                val weather: Weather
                if (isEmpty) {
                    weather = client.get(ApiRoutes.FORECAST) {
                        url {
                            parameters.append("key", BuildConfig.APP_ID)
                            parameters.append("q", requestParams)
                            parameters.append("days", "3")
                        }
                        contentType(ContentType.Application.Json)
                    }.body()
                } else {
                    weatherDao.clearTable()
                    weather = client.get(ApiRoutes.FORECAST) {
                        url {
                            parameters.append("key", BuildConfig.APP_ID)
                            parameters.append("q", requestParams)
                            parameters.append("days", "3")
                        }
                        contentType(ContentType.Application.Json)
                    }.body()
                }
                weatherDao.insertWeather(toWeatherDbo(weather))
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    companion object {

        const val NAME = "RefreshForecastDataWorker"

        fun makeDailyRequest(lat: Float, long: Float) = PeriodicWorkRequestBuilder<RefreshForecastDataWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "lat" to lat,
                    "long" to long
                )
            )
            .build()

        private fun calculateInitialDelay(): Long {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = now
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 30)
                set(Calendar.MILLISECOND, 0)
            }
            val timeToRefresh = calendar.timeInMillis - now
            return timeToRefresh
        }

    }

    class Factory @Inject constructor(
        private val weatherDao: WeatherDao,
        private val client: HttpClient,
    ): ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters,
        ): ListenableWorker {
            return RefreshForecastDataWorker(
                context,
                workerParameters,
                weatherDao,
                client,
            )
        }
    }

}