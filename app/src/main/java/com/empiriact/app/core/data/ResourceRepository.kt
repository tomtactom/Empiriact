package com.empiriact.app.data

import com.empiriact.app.data.db.ResourceDao
import kotlinx.coroutines.flow.Flow

class ResourceRepository(private val resourceDao: ResourceDao) {
    fun getAll(): Flow<List<Resource>> = resourceDao.getAll()

    suspend fun insert(resource: Resource) {
        resourceDao.insert(resource)
    }
}
