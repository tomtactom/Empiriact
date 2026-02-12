package com.empiriact.app.data

import com.empiriact.app.data.db.RoutineDao
import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val routineDao: RoutineDao) {
    fun getAll(): Flow<List<Routine>> = routineDao.getAll()

    suspend fun insert(routine: Routine) {
        routineDao.insert(routine)
    }
}
