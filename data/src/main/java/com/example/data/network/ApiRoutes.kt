package com.example.data.network

internal object ApiRoutes {

    private const val BASE_URL = "https://api.weatherapi.com/v1"

    const val FORECAST = "$BASE_URL/forecast.json"

    const val ASTRONOMY = "$BASE_URL/astronomy.json"

    const val SEARCH_ROUTE = "https://api.openweathermap.org/geo/1.0/reverse"

}