package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.dbo.WeatherDbo

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherDbo)

    @Query("SELECT * FROM weather_table")
    suspend fun getWeather(): WeatherDbo

    @Query("SELECT COUNT(*) FROM weather_table")
    suspend fun isEmpty(): Int

    @Query("DELETE FROM weather_table")
    suspend fun clearTable()

}
