package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empiriact.app.data.Evaluation
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(evaluation: Evaluation)

    @Query("SELECT * FROM evaluations ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Evaluation>>
}
