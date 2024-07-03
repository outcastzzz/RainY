package com.example.rainy.domain.useCase

import com.example.rainy.domain.entity.City
import com.example.rainy.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    suspend fun addToFavourite(city: City) = favouriteRepository
        .addToFavourite(city)

    suspend fun removeFromFavourite(cityName: String) = favouriteRepository
        .removeFromFavourite(cityName)

}