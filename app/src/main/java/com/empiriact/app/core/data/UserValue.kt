package com.empiriact.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_values")
data class UserValue(
    @PrimaryKey
    @ColumnInfo(name = "valueName")
    val name: String,
    val description: String,
    var importance: Int, // 0-10
    @ColumnInfo(name = "realization")
    var implementation: Int // 0-10
)
