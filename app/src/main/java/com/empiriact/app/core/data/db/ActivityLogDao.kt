package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empiriact.app.ui.screens.overview.ActivityAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {

    @Query("SELECT * FROM activity_logs WHERE localDate = :dateInt ORDER BY hour ASC")
    fun observeDay(dateInt: Int): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE localDate >= :startDateInt AND localDate < :endDateInt ORDER BY localDate DESC, hour DESC")
    fun observeRange(startDateInt: Int, endDateInt: Int): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ActivityLogEntity)

    @Query("DELETE FROM activity_logs WHERE localDate = :dateInt")
    suspend fun deleteDay(dateInt: Int)

    @Query("SELECT AVG(valence) FROM activity_logs WHERE localDate = :dateInt")
    fun observeDayAvgValence(dateInt: Int): Flow<Double?>

    @Query("SELECT * FROM activity_logs ORDER BY localDate DESC, hour DESC")
    fun getAll(): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE localDate >= :startOfDay AND localDate < :endOfDay ORDER BY localDate DESC, hour DESC")
    fun getLogsForDay(startOfDay: Int, endOfDay: Int): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE localDate >= :startOfWeek AND localDate < :endOfWeek ORDER BY localDate DESC, hour DESC")
    fun getLogsForWeek(startOfWeek: Int, endOfWeek: Int): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE localDate >= :startOfMonth AND localDate < :endOfMonth ORDER BY localDate DESC, hour DESC")
    fun getLogsForMonth(startOfMonth: Int, endOfMonth: Int): Flow<List<ActivityLogEntity>>

    @Query("SELECT DISTINCT activityText FROM activity_logs ORDER BY activityText ASC")
    fun getUniqueActivities(): Flow<List<String>>

    @Query("""
        SELECT activityText AS activity, AVG(valence) AS averageRating
        FROM activity_logs
        WHERE activityText != ''
        GROUP BY activityText
        HAVING COUNT(*) >= :threshold AND AVG(valence) > :minRating
        ORDER BY averageRating DESC
        """
    )
    fun getMoodBoosters(threshold: Int, minRating: Double): Flow<List<ActivityAnalysis>>

    @Query("""
        SELECT activityText AS activity, AVG(valence) AS averageRating
        FROM activity_logs
        WHERE activityText != ''
        GROUP BY activityText
        HAVING COUNT(*) >= :threshold AND AVG(valence) < :maxRating
        ORDER BY averageRating ASC
        """
    )
    fun getMoodDampers(threshold: Int, maxRating: Double): Flow<List<ActivityAnalysis>>
}
