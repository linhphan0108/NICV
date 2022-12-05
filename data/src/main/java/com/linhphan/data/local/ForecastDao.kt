package com.linhphan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ForecastDao {

    @Query("SELECT * FROM tblForecast WHERE cityName LIKE :cityName")
    suspend fun queryForecast(cityName: String): List<TblForecast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeForecasts(list: List<TblForecast>): LongArray

    @Query("DELETE FROM tblForecast")
    suspend fun deleteForecasts()

    @Query("DELETE FROM tblForecast WHERE date < :toDate")
    fun deleteForecasts(toDate: Long): Int

}