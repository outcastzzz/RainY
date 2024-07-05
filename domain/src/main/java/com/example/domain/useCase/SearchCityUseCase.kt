package com.example.domain.useCase

import com.example.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(q: String) = repository.searchCityUseCase(q)

}