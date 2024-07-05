package com.example.rainy.presentation.searchCity

import kotlinx.coroutines.flow.StateFlow

interface SearchCityComponent {

    val model: StateFlow<SearchCityStore.State>

    fun changeSearchQuery(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: com.example.domain.entity.City)

}