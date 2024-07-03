package com.example.rainy.domain.repository

import com.example.rainy.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteCities: Flow<List<City>>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityName: String)

}