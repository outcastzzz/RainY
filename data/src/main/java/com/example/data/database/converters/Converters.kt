package com.example.data.database.converters

import androidx.room.TypeConverter
import com.example.data.database.dbo.ForecastDayEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromForecastDayList(value: List<ForecastDayEntity>): String {
        val type = object : TypeToken<List<ForecastDayEntity>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toForecastDayList(value: String): List<ForecastDayEntity> {
        val type = object : TypeToken<List<ForecastDayEntity>>() {}.type
        return Gson().fromJson(value, type)
    }

}