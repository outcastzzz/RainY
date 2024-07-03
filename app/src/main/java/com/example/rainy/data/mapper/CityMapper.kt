package com.example.rainy.data.mapper

import com.example.rainy.data.database.dbo.CityDbModel
import com.example.rainy.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(
    name = name,
    lat = lat,
    long = long,
    country = country,
)

fun CityDbModel.toEntity(): City = City(
    name = name,
    lat = lat,
    long = long,
    country = country,
)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }