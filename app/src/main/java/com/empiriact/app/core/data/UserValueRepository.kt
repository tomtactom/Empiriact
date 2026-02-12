package com.empiriact.app.data

import com.empiriact.app.data.db.UserValueDao
import kotlinx.coroutines.flow.Flow

class UserValueRepository(private val dao: UserValueDao) {
    fun getAll(): Flow<List<UserValue>> = dao.getAll()

    suspend fun upsert(userValue: UserValue) {
        dao.upsert(userValue)
    }
}
