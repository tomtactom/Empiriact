package com.empiriact.app.data.repositories

import com.empiriact.app.data.room.daos.ModuleCompletionDao
import com.empiriact.app.data.room.entities.ModuleCompletion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Repository für Modul-Completions
 * Abstrahiert die Datenbank-Logik
 */
class ModuleCompletionRepository(
    private val moduleCompletionDao: ModuleCompletionDao
) {

    /**
     * Speichere eine neue Modul-Completion mit Rating
     */
    suspend fun saveModuleCompletion(
        moduleId: String,
        title: String,
        rating: Int?,
        ratingLabel: String?,
        isBookmarked: Boolean = false,
        readTimeMinutes: Int = 0,
        difficulty: String = ""
    ) {
        val completion = ModuleCompletion(
            moduleId = moduleId,
            title = title,
            completedAt = System.currentTimeMillis(),
            rating = rating,
            ratingLabel = ratingLabel,
            isBookmarked = isBookmarked,
            readTimeMinutes = readTimeMinutes,
            difficulty = difficulty
        )
        moduleCompletionDao.insertOrUpdateCompletion(completion)
    }

    /**
     * Lade eine spezifische Modul-Completion
     */
    suspend fun getCompletion(moduleId: String): ModuleCompletion? {
        return moduleCompletionDao.getCompletionByModuleId(moduleId)
    }

    /**
     * Lade alle Completions reaktiv
     */
    fun getAllCompletions(): Flow<List<ModuleCompletion>> {
        return moduleCompletionDao.getAllCompletions()
    }

    /**
     * Lade alle Completions mit Rating
     */
    fun getCompletionsWithRating(): Flow<List<ModuleCompletion>> {
        return moduleCompletionDao.getCompletionsWithRating()
    }

    /**
     * Zähle abgeschlossene Module
     */
    suspend fun getCompletionCount(): Int {
        return moduleCompletionDao.getCompletionCount()
    }

    /**
     * Lade alle gebookmarkte Module
     */
    fun getBookmarkedCompletions(): Flow<List<ModuleCompletion>> {
        return moduleCompletionDao.getBookmarkedCompletions()
    }

    /**
     * Aktualisiere nur das Rating eines Moduls
     */
    suspend fun updateRating(moduleId: String, rating: Int?, ratingLabel: String?) {
        moduleCompletionDao.updateRating(moduleId, rating, ratingLabel)
    }

    /**
     * Aktualisiere nur Bookmark-Status
     */
    suspend fun updateBookmark(moduleId: String, isBookmarked: Boolean) {
        moduleCompletionDao.updateBookmark(moduleId, isBookmarked)
    }

    /**
     * Lösche eine Completion
     */
    suspend fun deleteCompletion(moduleId: String) {
        val completion = moduleCompletionDao.getCompletionByModuleId(moduleId)
        if (completion != null) {
            moduleCompletionDao.deleteCompletion(completion)
        }
    }

    /**
     * Prüfe ob Modul bereits completed ist
     */
    suspend fun isModuleCompleted(moduleId: String): Boolean {
        return moduleCompletionDao.getCompletionByModuleId(moduleId) != null
    }

    /**
     * Prüfe ob Modul bereits bewertet wurde
     */
    suspend fun isModuleRated(moduleId: String): Boolean {
        val completion = moduleCompletionDao.getCompletionByModuleId(moduleId)
        return completion?.rating != null
    }
}

