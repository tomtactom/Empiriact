package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.`data`.Evaluation
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EvaluationDao_Impl(
  __db: RoomDatabase,
) : EvaluationDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfEvaluation: EntityInsertAdapter<Evaluation>
  init {
    this.__db = __db
    this.__insertAdapterOfEvaluation = object : EntityInsertAdapter<Evaluation>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `evaluations` (`id`,`score`,`type`,`timestamp`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Evaluation) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.score.toLong())
        statement.bindText(3, entity.type)
        statement.bindLong(4, entity.timestamp)
      }
    }
  }

  public override suspend fun insert(evaluation: Evaluation): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfEvaluation.insert(_connection, evaluation)
  }

  public override fun getAll(): Flow<List<Evaluation>> {
    val _sql: String = "SELECT * FROM evaluations ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("evaluations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<Evaluation> = mutableListOf()
        while (_stmt.step()) {
          val _item: Evaluation
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item = Evaluation(_tmpId,_tmpScore,_tmpType,_tmpTimestamp)
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
