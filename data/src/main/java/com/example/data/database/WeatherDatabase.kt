package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.database.converters.Converters
import com.example.data.database.dao.CityDao
import com.example.data.database.dao.WeatherDao
import com.example.data.database.dbo.CityDbo
import com.example.data.database.dbo.WeatherDbo

@Database(
    entities = [CityDbo::class, WeatherDbo::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun hourlyForecastDao(): WeatherDao

    companion object {

        private var db: WeatherDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): WeatherDatabase {
            db?.let { return it }
            synchronized(LOCK) {
                db?.let { return it }
                val database = Room.databaseBuilder(
                    context,
                    WeatherDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                db = database
                return database
            }
        }
    }

}