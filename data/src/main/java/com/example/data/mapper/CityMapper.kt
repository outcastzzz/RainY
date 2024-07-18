package com.example.data.mapper

import com.example.data.database.dbo.CityDbo
import com.example.domain.entity.City

fun City.toDbModel(): CityDbo = CityDbo(name = name, lat = lat, lon = lon)

fun CityDbo.toEntity(): City = City(lat, lon, name)

fun List<CityDbo>.toEntities(): List<City> = map { it.toEntity() }