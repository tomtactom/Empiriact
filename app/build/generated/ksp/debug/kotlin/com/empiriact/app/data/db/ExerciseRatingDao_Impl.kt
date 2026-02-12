package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.`data`.ExerciseAverageRating
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ExerciseRatingDao_Impl(
  __db: RoomDatabase,
) : ExerciseRatingDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExerciseRatingEntity: EntityInsertAdapter<ExerciseRatingEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfExerciseRatingEntity = object :
        EntityInsertAdapter<ExerciseRatingEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `exercise_ratings` (`id`,`exerciseId`,`rating`,`timestamp`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExerciseRatingEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.exerciseId)
        statement.bindLong(3, entity.rating.toLong())
        statement.bindLong(4, entity.timestamp)
      }
    }
  }

  public override suspend fun insert(rating: ExerciseRatingEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfExerciseRatingEntity.insert(_connection, rating)
  }

  public override fun getAllRatings(): Flow<List<ExerciseRatingEntity>> {
    val _sql: String = "SELECT * FROM exercise_ratings ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("exercise_ratings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExerciseId: Int = getColumnIndexOrThrow(_stmt, "exerciseId")
        val _columnIndexOfRating: Int = getColumnIndexOrThrow(_stmt, "rating")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<ExerciseRatingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExerciseRatingEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          val _tmpRating: Int
          _tmpRating = _stmt.getLong(_columnIndexOfRating).toInt()
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item = ExerciseRatingEntity(_tmpId,_tmpExerciseId,_tmpRating,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllAverageRatings(): Flow<List<ExerciseAverageRating>> {
    val _sql: String =
        "SELECT exerciseId, AVG(rating) as averageRating FROM exercise_ratings GROUP BY exerciseId"
    return createFlow(__db, false, arrayOf("exercise_ratings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfExerciseId: Int = 0
        val _columnIndexOfAverageRating: Int = 1
        val _result: MutableList<ExerciseAverageRating> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExerciseAverageRating
          val _tmpExerciseId: String
          _tmpExerciseId = _stmt.getText(_columnIndexOfExerciseId)
          val _tmpAverageRating: Double
          _tmpAverageRating = _stmt.getDouble(_columnIndexOfAverageRating)
          _item = ExerciseAverageRating(_tmpExerciseId,_tmpAverageRating)
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
