package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ExerciseReflectionDao_Impl(
  __db: RoomDatabase,
) : ExerciseReflectionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExerciseReflectionEntity:
      EntityInsertAdapter<ExerciseReflectionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfExerciseReflectionEntity = object :
        EntityInsertAdapter<ExerciseReflectionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `exercise_reflections` (`id`,`exerciseId`,`reflection`,`timestamp`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExerciseReflectionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.exerciseId)
        statement.bindText(3, entity.reflection)
        statement.bindLong(4, entity.timestamp)
      }
    }
  }

  public override suspend fun insert(reflection: ExerciseReflectionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfExerciseReflectionEntity.insert(_connection, reflection)
  }

  public override suspend fun getReflectionsByExerciseId(exerciseId: String):
      List<ExerciseReflectionEntity> {
    val _sql: String =
        "SELECT * FROM exercise_reflections WHERE exerciseId = ? ORDER BY timestamp DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, exerciseId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExerciseId: Int = getColumnIndexOrThrow(_stmt, "exerciseId")
        val _columnIndexOfReflection: Int = getColumnIndexOrThrow(_stmt, "reflection")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<ExerciseReflectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExerciseReflectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          val _tmpReflection: String
          _tmpReflection = _stmt.getText(_columnIndexOfReflection)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item = ExerciseReflectionEntity(_tmpId,_tmpExerciseId,_tmpReflection,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLatestReflections(limit: Int): List<ExerciseReflectionEntity> {
    val _sql: String = "SELECT * FROM exercise_reflections ORDER BY timestamp DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExerciseId: Int = getColumnIndexOrThrow(_stmt, "exerciseId")
        val _columnIndexOfReflection: Int = getColumnIndexOrThrow(_stmt, "reflection")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<ExerciseReflectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExerciseReflectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          val _tmpReflection: String
          _tmpReflection = _stmt.getText(_columnIndexOfReflection)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item = ExerciseReflectionEntity(_tmpId,_tmpExerciseId,_tmpReflection,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
