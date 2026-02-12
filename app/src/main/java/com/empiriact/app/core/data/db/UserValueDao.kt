package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empiriact.app.data.UserValue
import kotlinx.coroutines.flow.Flow

@Dao
interface UserValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(userValue: UserValue)

    @Query("SELECT * FROM user_values")
    fun getAll(): Flow<List<UserValue>>
}
