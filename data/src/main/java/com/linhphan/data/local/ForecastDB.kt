package com.linhphan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TblForecast::class], version = 1, exportSchema = false)
abstract class ForecastDB : RoomDatabase() {

    abstract fun getForecastDao(): ForecastDao

    companion object {

        private const val DATABASE_NAME = "WeatherForecast"
        @Volatile
        private var INSTANCE: ForecastDB? = null

        fun build(context: Context): ForecastDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, ForecastDB::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}