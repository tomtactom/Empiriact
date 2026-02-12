package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.`data`.UserValue
import javax.`annotation`.processing.Generated
import kotlin.Int
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
public class UserValueDao_Impl(
  __db: RoomDatabase,
) : UserValueDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserValue: EntityInsertAdapter<UserValue>
  init {
    this.__db = __db
    this.__insertAdapterOfUserValue = object : EntityInsertAdapter<UserValue>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_values` (`valueName`,`description`,`importance`,`realization`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserValue) {
        statement.bindText(1, entity.name)
        statement.bindText(2, entity.description)
        statement.bindLong(3, entity.importance.toLong())
        statement.bindLong(4, entity.implementation.toLong())
      }
    }
  }

  public override suspend fun upsert(userValue: UserValue): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfUserValue.insert(_connection, userValue)
  }

  public override fun getAll(): Flow<List<UserValue>> {
    val _sql: String = "SELECT * FROM user_values"
    return createFlow(__db, false, arrayOf("user_values")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "valueName")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfImportance: Int = getColumnIndexOrThrow(_stmt, "importance")
        val _columnIndexOfImplementation: Int = getColumnIndexOrThrow(_stmt, "realization")
        val _result: MutableList<UserValue> = mutableListOf()
        while (_stmt.step()) {
          val _item: UserValue
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpImportance: Int
          _tmpImportance = _stmt.getLong(_columnIndexOfImportance).toInt()
          val _tmpImplementation: Int
          _tmpImplementation = _stmt.getLong(_columnIndexOfImplementation).toInt()
          _item = UserValue(_tmpName,_tmpDescription,_tmpImportance,_tmpImplementation)
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
