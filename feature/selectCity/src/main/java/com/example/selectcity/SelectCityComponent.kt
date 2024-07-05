package com.example.selectcity

import com.example.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SelectCityComponent {

    val model: StateFlow<SelectCityStore.State>

    fun onClickBack()

    fun onClickAddCity()

    fun swipeRemoveCity(city: City)

}