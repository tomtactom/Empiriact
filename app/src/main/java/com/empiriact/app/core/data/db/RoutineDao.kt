package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empiriact.app.data.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine)

    @Query("SELECT * FROM routines ORDER BY id DESC")
    fun getAll(): Flow<List<Routine>>
}
