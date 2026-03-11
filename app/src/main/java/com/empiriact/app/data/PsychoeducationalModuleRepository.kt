package com.empiriact.app.data

import com.empiriact.app.data.db.PsychoeducationalModuleDao
import com.empiriact.app.data.db.PsychoeducationalModuleEntity
import kotlinx.coroutines.flow.Flow

class PsychoeducationalModuleRepository(
    private val dao: PsychoeducationalModuleDao
) {
    suspend fun createModule(
        moduleId: String,
        moduleTitle: String,
        category: String,
        isExample: Boolean = false
    ) {
        val module = PsychoeducationalModuleEntity(
            moduleId = moduleId,
            moduleTitle = moduleTitle,
            category = category,
            isExample = isExample
        )
        dao.insert(module)
    }

    fun getModule(moduleId: String): Flow<PsychoeducationalModuleEntity?> {
        return dao.getModule(moduleId)
    }

    fun getAllModules(): Flow<List<PsychoeducationalModuleEntity>> {
        return dao.getAllModules()
    }

    fun getBookmarkedModules(): Flow<List<PsychoeducationalModuleEntity>> {
        return dao.getBookmarkedModules()
    }

    fun getCompletedModules(): Flow<List<PsychoeducationalModuleEntity>> {
        return dao.getCompletedModules()
    }

    fun getCompletedModulesCount(): Flow<Int> {
        return dao.getCompletedModulesCount()
    }

    suspend fun toggleBookmark(moduleId: String, bookmarked: Boolean) {
        dao.toggleBookmark(moduleId, bookmarked)
    }

    suspend fun markAsCompleted(moduleId: String) {
        dao.markAsCompleted(moduleId)
    }

    suspend fun setModuleRating(moduleId: String, rating: Int) {
        dao.setRating(moduleId, rating)
    }
}

