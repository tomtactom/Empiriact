package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empiriact.app.data.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: Resource)

    @Query("SELECT * FROM resources ORDER BY id DESC")
    fun getAll(): Flow<List<Resource>>
}
