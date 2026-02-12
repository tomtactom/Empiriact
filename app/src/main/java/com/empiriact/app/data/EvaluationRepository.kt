package com.empiriact.app.data

import com.empiriact.app.data.db.EvaluationDao
import kotlinx.coroutines.flow.Flow

class EvaluationRepository(private val evaluationDao: EvaluationDao) {
    fun getAll(): Flow<List<Evaluation>> = evaluationDao.getAll()

    suspend fun insert(evaluation: Evaluation) {
        evaluationDao.insert(evaluation)
    }
}
