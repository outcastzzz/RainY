package com.example.rainy.domain.repository

import com.example.rainy.domain.entity.City

interface SearchRepository {

    suspend fun searchCityUseCase(q: String): List<City>

}