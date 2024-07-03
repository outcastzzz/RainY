package com.example.rainy.data.network

object ApiRoutes {

    private const val BASE_URL = "https://api.weatherapi.com/v1"

    const val CURRENT_WEATHER = "$BASE_URL/current.json"

    const val FORECAST = "$BASE_URL/forecast.json"

    const val ASTRONOMY = "$BASE_URL/astronomy.json"

    const val SEARCH_ROUTE = "https://api.openweathermap.org/geo/1.0/reverse"

}