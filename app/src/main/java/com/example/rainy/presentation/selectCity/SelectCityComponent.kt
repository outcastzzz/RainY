package com.example.rainy.presentation.selectCity

import kotlinx.coroutines.flow.StateFlow

interface SelectCityComponent {

    val model: StateFlow<SelectCityStore.State>

    fun onClickBack()

    fun onClickAddCity()

    fun swipeRemoveCity(city: com.example.domain.entity.City)

}