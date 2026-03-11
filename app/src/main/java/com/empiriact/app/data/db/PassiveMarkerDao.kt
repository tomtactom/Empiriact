package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PassiveMarkerDao {

    @Query("SELECT * FROM passive_marker_hourly WHERE localDate = :localDate ORDER BY hour ASC")
    fun observeDay(localDate: Int): Flow<List<PassiveMarkerHourlyEntity>>

    @Query("SELECT * FROM passive_marker_hourly WHERE localDate >= :startDate AND localDate < :endDate ORDER BY localDate ASC, hour ASC")
    fun observeRange(startDate: Int, endDate: Int): Flow<List<PassiveMarkerHourlyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: PassiveMarkerHourlyEntity)
}
