package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import java.time.LocalDate
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GratitudeDao_Impl(
  __db: RoomDatabase,
) : GratitudeDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGratitudeEntity: EntityInsertAdapter<GratitudeEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfGratitudeEntity = object : EntityInsertAdapter<GratitudeEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `gratitude_logs` (`id`,`date`,`entry`,`timestamp`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GratitudeEntity) {
        statement.bindLong(1, entity.id.toLong())
        val _tmp: Long? = __converters.dateToTimestamp(entity.date)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmp)
        }
        statement.bindText(3, entity.entry)
        statement.bindLong(4, entity.timestamp)
      }
    }
  }

  public override suspend fun upsert(log: GratitudeEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfGratitudeEntity.insert(_connection, log)
  }

  public override fun getGratitudeLogForDate(date: LocalDate): Flow<GratitudeEntity?> {
    val _sql: String = "SELECT * FROM gratitude_logs WHERE date = ?"
    return createFlow(__db, false, arrayOf("gratitude_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Long? = __converters.dateToTimestamp(date)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, _tmp)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfEntry: Int = getColumnIndexOrThrow(_stmt, "entry")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: GratitudeEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpDate: LocalDate
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfDate)
          }
          val _tmp_2: LocalDate? = __converters.fromTimestamp(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.time.LocalDate', but it was NULL.")
          } else {
            _tmpDate = _tmp_2
          }
          val _tmpEntry: String
          _tmpEntry = _stmt.getText(_columnIndexOfEntry)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result = GratitudeEntity(_tmpId,_tmpDate,_tmpEntry,_tmpTimestamp)
        } else {
          _result = null
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
