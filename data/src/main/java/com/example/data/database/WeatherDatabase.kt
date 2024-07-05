package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.dao.CityDao
import com.example.data.database.dbo.CityDbModel

@Database(
    entities = [CityDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun cityDao(): CityDao


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