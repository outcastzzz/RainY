package com.example.rainy.domain.useCase

import com.example.rainy.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    operator fun invoke() = favouriteRepository.favouriteCities

}