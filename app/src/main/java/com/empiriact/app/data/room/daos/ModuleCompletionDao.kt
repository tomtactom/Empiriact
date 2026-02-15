package com.empiriact.app.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.empiriact.app.data.room.entities.ModuleCompletion
import kotlinx.coroutines.flow.Flow

/**
 * DAO für Modul-Completions
 * Verwaltet: Speichern, Laden, Aktualisieren von Modul-Bewertungen
 */
@Dao
interface ModuleCompletionDao {

    /**
     * Speichere eine neue Modul-Completion oder aktualisiere bestehende
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCompletion(completion: ModuleCompletion)

    /**
     * Lade eine spezifische Modul-Completion
     */
    @Query("SELECT * FROM module_completions WHERE moduleId = :moduleId")
    suspend fun getCompletionByModuleId(moduleId: String): ModuleCompletion?

    /**
     * Lade alle Modul-Completions als Flow (reaktiv)
     */
    @Query("SELECT * FROM module_completions ORDER BY completedAt DESC")
    fun getAllCompletions(): Flow<List<ModuleCompletion>>

    /**
     * Lade alle Modul-Completions mit Rating
     */
    @Query("SELECT * FROM module_completions WHERE rating IS NOT NULL ORDER BY completedAt DESC")
    fun getCompletionsWithRating(): Flow<List<ModuleCompletion>>

    /**
     * Zähle abgeschlossene Module
     */
    @Query("SELECT COUNT(*) FROM module_completions")
    suspend fun getCompletionCount(): Int

    /**
     * Finde alle gebookmarkte Module
     */
    @Query("SELECT * FROM module_completions WHERE isBookmarked = 1 ORDER BY completedAt DESC")
    fun getBookmarkedCompletions(): Flow<List<ModuleCompletion>>

    /**
     * Aktualisiere nur das Rating
     */
    @Query("UPDATE module_completions SET rating = :rating, ratingLabel = :ratingLabel WHERE moduleId = :moduleId")
    suspend fun updateRating(moduleId: String, rating: Int?, ratingLabel: String?)

    /**
     * Aktualisiere nur Bookmark-Status
     */
    @Query("UPDATE module_completions SET isBookmarked = :isBookmarked WHERE moduleId = :moduleId")
    suspend fun updateBookmark(moduleId: String, isBookmarked: Boolean)

    /**
     * Lösche eine Completion
     */
    @Delete
    suspend fun deleteCompletion(completion: ModuleCompletion)

    /**
     * Lösche alle Completions
     */
    @Query("DELETE FROM module_completions")
    suspend fun deleteAllCompletions()
}

