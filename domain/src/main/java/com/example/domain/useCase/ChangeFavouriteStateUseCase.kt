package com.example.domain.useCase

import com.example.domain.entity.City
import com.example.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    suspend fun addToFavourite(city: City) = favouriteRepository
        .addToFavourite(city)

    suspend fun removeFromFavourite(cityName: String) = favouriteRepository
        .removeFromFavourite(cityName)

}