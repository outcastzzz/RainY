package com.example.rainy.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object KtorClient {

    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(Android) {

        install(Logging) {
            logger = CustomHttpLogger()
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

        install(ContentNegotiation) {
            register(ContentType.Application.Json, KotlinxSerializationConverter(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                explicitNulls = false
            }))
        }

    }

}

class CustomHttpLogger: Logger {
    override fun log(message: String) {
        Log.d("loggerTag", message)
    }
}