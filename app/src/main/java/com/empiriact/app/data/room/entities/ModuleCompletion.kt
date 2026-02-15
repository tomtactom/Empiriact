package com.empiriact.app.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Datenbank-Entity für abgeschlossene psychoedukative Module
 * Speichert: Modul-ID, Completion-Datum, User-Rating, Bookmark-Status
 */
@Entity(tableName = "module_completions")
data class ModuleCompletion(
    @PrimaryKey(autoGenerate = false)
    val moduleId: String,

    val title: String,
    val completedAt: Long, // Timestamp
    val rating: Int? = null, // -2, -1, 0, 1, 2 oder null
    val ratingLabel: String? = null, // "--", "-", "0", "+", "++"
    val isBookmarked: Boolean = false,

    // Metadata
    val readTimeMinutes: Int = 0,
    val difficulty: String = ""
)

