package com.example.rainy.domain.useCase

import com.example.rainy.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(q: String) = repository.searchCityUseCase(q)

}