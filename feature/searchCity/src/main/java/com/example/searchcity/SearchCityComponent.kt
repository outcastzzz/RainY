package com.example.searchcity

import com.example.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchCityComponent {

    val model: StateFlow<SearchCityStore.State>

    fun changeSearchQuery(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: City)

}