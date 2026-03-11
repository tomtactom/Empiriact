package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "psychoeducational_modules")
data class PsychoeducationalModuleEntity(
    @PrimaryKey val moduleId: String,
    val moduleTitle: String,
    val category: String, // z.B. "Grübeln", "Emotionsregulation", etc.
    val isCompleted: Boolean = false,
    val rating: Int? = null, // -2 bis +2
    val feedback: String? = null,
    val completedAt: Long? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false,
    val isExample: Boolean = false // Markiert Beispielmodule
)

