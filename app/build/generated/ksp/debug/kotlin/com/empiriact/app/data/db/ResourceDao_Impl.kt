package com.empiriact.app.`data`.db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.empiriact.app.`data`.Resource
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
public class ResourceDao_Impl(
  __db: RoomDatabase,
) : ResourceDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfResource: EntityInsertAdapter<Resource>
  init {
    this.__db = __db
    this.__insertAdapterOfResource = object : EntityInsertAdapter<Resource>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `resources` (`id`,`name`,`description`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Resource) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.description)
      }
    }
  }

  public override suspend fun insert(resource: Resource): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfResource.insert(_connection, resource)
  }

  public override fun getAll(): Flow<List<Resource>> {
    val _sql: String = "SELECT * FROM resources ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("resources")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _result: MutableList<Resource> = mutableListOf()
        while (_stmt.step()) {
          val _item: Resource
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          _item = Resource(_tmpId,_tmpName,_tmpDescription)
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
