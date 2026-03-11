package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PsychoeducationalModuleDao {
    @Insert
    suspend fun insert(module: PsychoeducationalModuleEntity)

    @Update
    suspend fun update(module: PsychoeducationalModuleEntity)

    @Query("SELECT * FROM psychoeducational_modules WHERE moduleId = :moduleId")
    fun getModule(moduleId: String): Flow<PsychoeducationalModuleEntity?>

    @Query("SELECT * FROM psychoeducational_modules ORDER BY startedAt DESC")
    fun getAllModules(): Flow<List<PsychoeducationalModuleEntity>>

    @Query("SELECT * FROM psychoeducational_modules WHERE isBookmarked = 1 ORDER BY startedAt DESC")
    fun getBookmarkedModules(): Flow<List<PsychoeducationalModuleEntity>>

    @Query("SELECT * FROM psychoeducational_modules WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedModules(): Flow<List<PsychoeducationalModuleEntity>>

    @Query("SELECT COUNT(*) FROM psychoeducational_modules WHERE isCompleted = 1")
    fun getCompletedModulesCount(): Flow<Int>

    @Query("UPDATE psychoeducational_modules SET isBookmarked = :bookmarked WHERE moduleId = :moduleId")
    suspend fun toggleBookmark(moduleId: String, bookmarked: Boolean)

    @Query("UPDATE psychoeducational_modules SET isCompleted = 1, completedAt = :timestamp WHERE moduleId = :moduleId")
    suspend fun markAsCompleted(moduleId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE psychoeducational_modules SET rating = :rating WHERE moduleId = :moduleId")
    suspend fun setRating(moduleId: String, rating: Int)
}

