package com.example.data.repository

import com.example.data.BuildConfig
import com.example.data.network.ApiRoutes
import com.example.domain.entity.City
import com.example.domain.repository.SearchRepository
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
        return client.get(ApiRoutes.SEARCH_CITIES_ROUTE) {
            url {
                parameters.append("q", q)
                parameters.append("api_key", BuildConfig.GEO_KEY)
            }
            contentType(ContentType.Application.Json)
        }.body<List<City>>().take(3)
    }

}
