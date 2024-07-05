package com.example.data.repository

import com.example.data.database.dao.CityDao
import com.example.data.mapper.toDbModel
import com.example.data.mapper.toEntities
import com.example.domain.entity.City
import com.example.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val cityDao: CityDao
): FavouriteRepository {

    override val favouriteCities: Flow<List<City>> = cityDao.getFavouriteCities()
        .map { it.toEntities() }

    override suspend fun addToFavourite(city: City) = cityDao
        .addToFavourite(city.toDbModel())

    override suspend fun removeFromFavourite(cityName: String) = cityDao
        .removeFromFavourite(cityName)
}