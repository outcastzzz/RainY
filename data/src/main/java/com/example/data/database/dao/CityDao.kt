package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.dbo.CityDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city_table")
    fun getFavouriteCities(): Flow<List<CityDbo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbo: CityDbo)

    @Query("DELETE FROM city_table WHERE name=:cityName")
    suspend fun removeFromFavourite(cityName: String)

}