package com.example.rainy.presentation.selectCity

import com.example.rainy.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SelectCityComponent {

    val model: StateFlow<SelectCityStore.State>

    fun onClickBack()

    fun onClickAddCity()

    fun swipeRemoveCity(city: City)

}