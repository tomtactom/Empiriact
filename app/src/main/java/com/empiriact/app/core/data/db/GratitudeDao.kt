package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GratitudeDao {
    @Query("SELECT * FROM gratitude_logs WHERE date = :date")
    fun getGratitudeLogForDate(date: LocalDate): Flow<GratitudeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: GratitudeEntity)
}
