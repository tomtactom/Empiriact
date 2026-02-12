package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.`data`.Routine
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
public class RoutineDao_Impl(
  __db: RoomDatabase,
) : RoutineDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfRoutine: EntityInsertAdapter<Routine>
  init {
    this.__db = __db
    this.__insertAdapterOfRoutine = object : EntityInsertAdapter<Routine>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `routines` (`id`,`name`,`description`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Routine) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.description)
      }
    }
  }

  public override suspend fun insert(routine: Routine): Unit = performSuspending(__db, false, true)
      { _connection ->
    __insertAdapterOfRoutine.insert(_connection, routine)
  }

  public override fun getAll(): Flow<List<Routine>> {
    val _sql: String = "SELECT * FROM routines ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("routines")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<Routine> = mutableListOf()
        while (_stmt.step()) {
          val _item: Routine
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item = Routine(_tmpId,_tmpName,_tmpDescription)
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
