package com.example.data.workers

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshForecastDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val weatherDao: WeatherDao,
    private val client: HttpClient,
) : CoroutineWorker(context, workerParameters) {

    private val mutex = Mutex()

    override suspend fun doWork(): Result {
        while (true) {
            Log.d("worker", "smth happening")
            val lat = inputData.getFloat("lat", 0.0f)
            val long = inputData.getFloat("long", 0.0f)
            val requestParams = "$lat, $long"
            Log.d("worker", requestParams)
            mutex.withLock {
                val isEmpty = when (weatherDao.isEmpty()) {
                    0 -> true
                    else -> false
                }
                try {
                    var weather: Weather? = null
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
                    weather?.let { toWeatherDbo(it) }?.let { weatherDao.insertWeather(it) }
                    Result.success()
                } catch (e: Exception) {
                    Result.retry()
                }
            }
            val hours = 12
            val hoursInMilliseconds = TimeUnit.HOURS.toMillis(hours.toLong())
            delay(hoursInMilliseconds)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    companion object {

        const val NAME = "RefreshForecastDataWorker"

        private val delay = getDelayUntilNextFullHour()

        fun makeRequest(lat: Float, long: Float) =
            OneTimeWorkRequestBuilder<RefreshForecastDataWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "lat" to lat,
                        "long" to long
                    )
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        private fun getDelayUntilNextFullHour(): Long {
            val now = Calendar.getInstance()
            val nextHour = now.clone() as Calendar
            nextHour.add(Calendar.HOUR, 1)
            nextHour.set(Calendar.MINUTE, 0)
            nextHour.set(Calendar.SECOND, 0)
            nextHour.set(Calendar.MILLISECOND, 0)

            return nextHour.timeInMillis - now.timeInMillis
        }
    }

    class Factory @Inject constructor(
        private val weatherDao: WeatherDao,
        private val client: HttpClient,
    ) : ChildWorkerFactory {
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