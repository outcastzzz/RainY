package com.example.rainy.data.repository

import com.example.rainy.BuildConfig
import com.example.rainy.domain.entity.City
import com.example.rainy.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val client: HttpClient
): SearchRepository {

    override suspend fun searchCityUseCase(q: String): List<City> {
        return client.get("https://api.openweathermap.org/geo/1.0/direct") {
            url {
                parameters.append("q", q)
                parameters.append("appid", BuildConfig.WEATHER_KEY)
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

}
