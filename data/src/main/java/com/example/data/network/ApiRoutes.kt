package com.example.data.network

internal object ApiRoutes {

    private const val BASE_URL = "https://api.weatherapi.com/v1"

    const val FORECAST = "$BASE_URL/forecast.json"

    const val ASTRONOMY = "$BASE_URL/astronomy.json"

    const val SEARCH_CITY_NAME_ROUTE = " https://geocode.maps.co/reverse"

    const val SEARCH_CITIES_ROUTE = "https://geocode.maps.co/search"

}