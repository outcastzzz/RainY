package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.dbo.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city_table")
    fun getFavouriteCities(): Flow<List<CityDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbModel: CityDbModel)

    @Query("DELETE FROM city_table WHERE name=:cityName")
    suspend fun removeFromFavourite(cityName: String)

}