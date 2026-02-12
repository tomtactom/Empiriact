package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.ui.screens.overview.ActivityAnalysis
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
public class ActivityLogDao_Impl(
  __db: RoomDatabase,
) : ActivityLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfActivityLogEntity: EntityInsertAdapter<ActivityLogEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfActivityLogEntity = object : EntityInsertAdapter<ActivityLogEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `activity_logs` (`key`,`localDate`,`hour`,`activityText`,`valence`,`updatedAtEpochMs`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityLogEntity) {
        statement.bindText(1, entity.key)
        statement.bindLong(2, entity.localDate.toLong())
        statement.bindLong(3, entity.hour.toLong())
        statement.bindText(4, entity.activityText)
        statement.bindLong(5, entity.valence.toLong())
        statement.bindLong(6, entity.updatedAtEpochMs)
      }
    }
  }

  public override suspend fun upsert(entity: ActivityLogEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfActivityLogEntity.insert(_connection, entity)
  }

  public override fun observeDay(dateInt: Int): Flow<List<ActivityLogEntity>> {
    val _sql: String = "SELECT * FROM activity_logs WHERE localDate = ? ORDER BY hour ASC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dateInt.toLong())
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeRange(startDateInt: Int, endDateInt: Int):
      Flow<List<ActivityLogEntity>> {
    val _sql: String =
        "SELECT * FROM activity_logs WHERE localDate >= ? AND localDate < ? ORDER BY localDate DESC, hour DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDateInt.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDateInt.toLong())
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeDayAvgValence(dateInt: Int): Flow<Double?> {
    val _sql: String = "SELECT AVG(valence) FROM activity_logs WHERE localDate = ?"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dateInt.toLong())
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAll(): Flow<List<ActivityLogEntity>> {
    val _sql: String = "SELECT * FROM activity_logs ORDER BY localDate DESC, hour DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForDay(startOfDay: Int, endOfDay: Int): Flow<List<ActivityLogEntity>> {
    val _sql: String =
        "SELECT * FROM activity_logs WHERE localDate >= ? AND localDate < ? ORDER BY localDate DESC, hour DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfDay.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfDay.toLong())
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForWeek(startOfWeek: Int, endOfWeek: Int):
      Flow<List<ActivityLogEntity>> {
    val _sql: String =
        "SELECT * FROM activity_logs WHERE localDate >= ? AND localDate < ? ORDER BY localDate DESC, hour DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfWeek.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfWeek.toLong())
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForMonth(startOfMonth: Int, endOfMonth: Int):
      Flow<List<ActivityLogEntity>> {
    val _sql: String =
        "SELECT * FROM activity_logs WHERE localDate >= ? AND localDate < ? ORDER BY localDate DESC, hour DESC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfMonth.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfMonth.toLong())
        val _columnIndexOfKey: Int = getColumnIndexOrThrow(_stmt, "key")
        val _columnIndexOfLocalDate: Int = getColumnIndexOrThrow(_stmt, "localDate")
        val _columnIndexOfHour: Int = getColumnIndexOrThrow(_stmt, "hour")
        val _columnIndexOfActivityText: Int = getColumnIndexOrThrow(_stmt, "activityText")
        val _columnIndexOfValence: Int = getColumnIndexOrThrow(_stmt, "valence")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _result: MutableList<ActivityLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityLogEntity
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfKey)
          val _tmpLocalDate: Int
          _tmpLocalDate = _stmt.getLong(_columnIndexOfLocalDate).toInt()
          val _tmpHour: Int
          _tmpHour = _stmt.getLong(_columnIndexOfHour).toInt()
          val _tmpActivityText: String
          _tmpActivityText = _stmt.getText(_columnIndexOfActivityText)
          val _tmpValence: Int
          _tmpValence = _stmt.getLong(_columnIndexOfValence).toInt()
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          _item =
              ActivityLogEntity(_tmpKey,_tmpLocalDate,_tmpHour,_tmpActivityText,_tmpValence,_tmpUpdatedAtEpochMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getUniqueActivities(): Flow<List<String>> {
    val _sql: String = "SELECT DISTINCT activityText FROM activity_logs ORDER BY activityText ASC"
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMoodBoosters(threshold: Int, minRating: Double):
      Flow<List<ActivityAnalysis>> {
    val _sql: String = """
        |
        |        SELECT activityText AS activity, AVG(valence) AS averageRating
        |        FROM activity_logs
        |        WHERE activityText != ''
        |        GROUP BY activityText
        |        HAVING COUNT(*) >= ? AND AVG(valence) > ?
        |        ORDER BY averageRating DESC
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threshold.toLong())
        _argIndex = 2
        _stmt.bindDouble(_argIndex, minRating)
        val _columnIndexOfActivity: Int = 0
        val _columnIndexOfAverageRating: Int = 1
        val _result: MutableList<ActivityAnalysis> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityAnalysis
          val _tmpActivity: String
          _tmpActivity = _stmt.getText(_columnIndexOfActivity)
          val _tmpAverageRating: Double
          _tmpAverageRating = _stmt.getDouble(_columnIndexOfAverageRating)
          _item = ActivityAnalysis(_tmpActivity,_tmpAverageRating)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMoodDampers(threshold: Int, maxRating: Double):
      Flow<List<ActivityAnalysis>> {
    val _sql: String = """
        |
        |        SELECT activityText AS activity, AVG(valence) AS averageRating
        |        FROM activity_logs
        |        WHERE activityText != ''
        |        GROUP BY activityText
        |        HAVING COUNT(*) >= ? AND AVG(valence) < ?
        |        ORDER BY averageRating ASC
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("activity_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threshold.toLong())
        _argIndex = 2
        _stmt.bindDouble(_argIndex, maxRating)
        val _columnIndexOfActivity: Int = 0
        val _columnIndexOfAverageRating: Int = 1
        val _result: MutableList<ActivityAnalysis> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityAnalysis
          val _tmpActivity: String
          _tmpActivity = _stmt.getText(_columnIndexOfActivity)
          val _tmpAverageRating: Double
          _tmpAverageRating = _stmt.getDouble(_columnIndexOfAverageRating)
          _item = ActivityAnalysis(_tmpActivity,_tmpAverageRating)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteDay(dateInt: Int) {
    val _sql: String = "DELETE FROM activity_logs WHERE localDate = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dateInt.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
