package com.empiriact.app.`data`.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EmpiriactDatabase_Impl : EmpiriactDatabase() {
  private val _activityLogDao: Lazy<ActivityLogDao> = lazy {
    ActivityLogDao_Impl(this)
  }

  private val _routineDao: Lazy<RoutineDao> = lazy {
    RoutineDao_Impl(this)
  }

  private val _resourceDao: Lazy<ResourceDao> = lazy {
    ResourceDao_Impl(this)
  }

  private val _evaluationDao: Lazy<EvaluationDao> = lazy {
    EvaluationDao_Impl(this)
  }

  private val _userValueDao: Lazy<UserValueDao> = lazy {
    UserValueDao_Impl(this)
  }

  private val _exerciseRatingDao: Lazy<ExerciseRatingDao> = lazy {
    ExerciseRatingDao_Impl(this)
  }

  private val _exerciseReflectionDao: Lazy<ExerciseReflectionDao> = lazy {
    ExerciseReflectionDao_Impl(this)
  }

  private val _gratitudeDao: Lazy<GratitudeDao> = lazy {
    GratitudeDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(10,
        "3f8d0054146b851b632ce49380caa38a", "f59e3ec4dadf22a00157315fa3e0d25e") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `activity_logs` (`key` TEXT NOT NULL, `localDate` INTEGER NOT NULL, `hour` INTEGER NOT NULL, `activityText` TEXT NOT NULL, `valence` INTEGER NOT NULL, `updatedAtEpochMs` INTEGER NOT NULL, PRIMARY KEY(`key`))")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_activity_logs_localDate_hour` ON `activity_logs` (`localDate`, `hour`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `routines` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `resources` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `evaluations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `score` INTEGER NOT NULL, `type` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_values` (`valueName` TEXT NOT NULL, `description` TEXT NOT NULL, `importance` INTEGER NOT NULL, `realization` INTEGER NOT NULL, PRIMARY KEY(`valueName`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `exercise_ratings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exerciseId` TEXT NOT NULL, `rating` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `exercise_reflections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exerciseId` TEXT NOT NULL, `reflection` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `gratitude_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `entry` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3f8d0054146b851b632ce49380caa38a')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `activity_logs`")
        connection.execSQL("DROP TABLE IF EXISTS `routines`")
        connection.execSQL("DROP TABLE IF EXISTS `resources`")
        connection.execSQL("DROP TABLE IF EXISTS `evaluations`")
        connection.execSQL("DROP TABLE IF EXISTS `user_values`")
        connection.execSQL("DROP TABLE IF EXISTS `exercise_ratings`")
        connection.execSQL("DROP TABLE IF EXISTS `exercise_reflections`")
        connection.execSQL("DROP TABLE IF EXISTS `gratitude_logs`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsActivityLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsActivityLogs.put("key", TableInfo.Column("key", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityLogs.put("localDate", TableInfo.Column("localDate", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityLogs.put("hour", TableInfo.Column("hour", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityLogs.put("activityText", TableInfo.Column("activityText", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityLogs.put("valence", TableInfo.Column("valence", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityLogs.put("updatedAtEpochMs", TableInfo.Column("updatedAtEpochMs", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysActivityLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesActivityLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesActivityLogs.add(TableInfo.Index("index_activity_logs_localDate_hour", true,
            listOf("localDate", "hour"), listOf("ASC", "ASC")))
        val _infoActivityLogs: TableInfo = TableInfo("activity_logs", _columnsActivityLogs,
            _foreignKeysActivityLogs, _indicesActivityLogs)
        val _existingActivityLogs: TableInfo = read(connection, "activity_logs")
        if (!_infoActivityLogs.equals(_existingActivityLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |activity_logs(com.empiriact.app.data.db.ActivityLogEntity).
              | Expected:
              |""".trimMargin() + _infoActivityLogs + """
              |
              | Found:
              |""".trimMargin() + _existingActivityLogs)
        }
        val _columnsRoutines: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRoutines.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRoutines.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRoutines.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRoutines: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRoutines: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoRoutines: TableInfo = TableInfo("routines", _columnsRoutines, _foreignKeysRoutines,
            _indicesRoutines)
        val _existingRoutines: TableInfo = read(connection, "routines")
        if (!_infoRoutines.equals(_existingRoutines)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |routines(com.empiriact.app.data.Routine).
              | Expected:
              |""".trimMargin() + _infoRoutines + """
              |
              | Found:
              |""".trimMargin() + _existingRoutines)
        }
        val _columnsResources: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsResources.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsResources.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsResources.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysResources: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesResources: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoResources: TableInfo = TableInfo("resources", _columnsResources,
            _foreignKeysResources, _indicesResources)
        val _existingResources: TableInfo = read(connection, "resources")
        if (!_infoResources.equals(_existingResources)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |resources(com.empiriact.app.data.Resource).
              | Expected:
              |""".trimMargin() + _infoResources + """
              |
              | Found:
              |""".trimMargin() + _existingResources)
        }
        val _columnsEvaluations: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEvaluations.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvaluations.put("score", TableInfo.Column("score", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvaluations.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEvaluations.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEvaluations: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEvaluations: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEvaluations: TableInfo = TableInfo("evaluations", _columnsEvaluations,
            _foreignKeysEvaluations, _indicesEvaluations)
        val _existingEvaluations: TableInfo = read(connection, "evaluations")
        if (!_infoEvaluations.equals(_existingEvaluations)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |evaluations(com.empiriact.app.data.Evaluation).
              | Expected:
              |""".trimMargin() + _infoEvaluations + """
              |
              | Found:
              |""".trimMargin() + _existingEvaluations)
        }
        val _columnsUserValues: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserValues.put("valueName", TableInfo.Column("valueName", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserValues.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserValues.put("importance", TableInfo.Column("importance", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserValues.put("realization", TableInfo.Column("realization", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserValues: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserValues: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserValues: TableInfo = TableInfo("user_values", _columnsUserValues,
            _foreignKeysUserValues, _indicesUserValues)
        val _existingUserValues: TableInfo = read(connection, "user_values")
        if (!_infoUserValues.equals(_existingUserValues)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_values(com.empiriact.app.data.UserValue).
              | Expected:
              |""".trimMargin() + _infoUserValues + """
              |
              | Found:
              |""".trimMargin() + _existingUserValues)
        }
        val _columnsExerciseRatings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExerciseRatings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseRatings.put("exerciseId", TableInfo.Column("exerciseId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseRatings.put("rating", TableInfo.Column("rating", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseRatings.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExerciseRatings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesExerciseRatings: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoExerciseRatings: TableInfo = TableInfo("exercise_ratings", _columnsExerciseRatings,
            _foreignKeysExerciseRatings, _indicesExerciseRatings)
        val _existingExerciseRatings: TableInfo = read(connection, "exercise_ratings")
        if (!_infoExerciseRatings.equals(_existingExerciseRatings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |exercise_ratings(com.empiriact.app.data.db.ExerciseRatingEntity).
              | Expected:
              |""".trimMargin() + _infoExerciseRatings + """
              |
              | Found:
              |""".trimMargin() + _existingExerciseRatings)
        }
        val _columnsExerciseReflections: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExerciseReflections.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseReflections.put("exerciseId", TableInfo.Column("exerciseId", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseReflections.put("reflection", TableInfo.Column("reflection", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsExerciseReflections.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExerciseReflections: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesExerciseReflections: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoExerciseReflections: TableInfo = TableInfo("exercise_reflections",
            _columnsExerciseReflections, _foreignKeysExerciseReflections,
            _indicesExerciseReflections)
        val _existingExerciseReflections: TableInfo = read(connection, "exercise_reflections")
        if (!_infoExerciseReflections.equals(_existingExerciseReflections)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |exercise_reflections(com.empiriact.app.data.db.ExerciseReflectionEntity).
              | Expected:
              |""".trimMargin() + _infoExerciseReflections + """
              |
              | Found:
              |""".trimMargin() + _existingExerciseReflections)
        }
        val _columnsGratitudeLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGratitudeLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGratitudeLogs.put("date", TableInfo.Column("date", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGratitudeLogs.put("entry", TableInfo.Column("entry", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsGratitudeLogs.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGratitudeLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGratitudeLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGratitudeLogs: TableInfo = TableInfo("gratitude_logs", _columnsGratitudeLogs,
            _foreignKeysGratitudeLogs, _indicesGratitudeLogs)
        val _existingGratitudeLogs: TableInfo = read(connection, "gratitude_logs")
        if (!_infoGratitudeLogs.equals(_existingGratitudeLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |gratitude_logs(com.empiriact.app.data.db.GratitudeEntity).
              | Expected:
              |""".trimMargin() + _infoGratitudeLogs + """
              |
              | Found:
              |""".trimMargin() + _existingGratitudeLogs)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "activity_logs", "routines",
        "resources", "evaluations", "user_values", "exercise_ratings", "exercise_reflections",
        "gratitude_logs")
  }

  public override fun clearAllTables() {
    super.performClear(false, "activity_logs", "routines", "resources", "evaluations",
        "user_values", "exercise_ratings", "exercise_reflections", "gratitude_logs")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ActivityLogDao::class, ActivityLogDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(RoutineDao::class, RoutineDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ResourceDao::class, ResourceDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(EvaluationDao::class, EvaluationDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserValueDao::class, UserValueDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ExerciseRatingDao::class, ExerciseRatingDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ExerciseReflectionDao::class,
        ExerciseReflectionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GratitudeDao::class, GratitudeDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun activityLogDao(): ActivityLogDao = _activityLogDao.value

  public override fun routineDao(): RoutineDao = _routineDao.value

  public override fun resourceDao(): ResourceDao = _resourceDao.value

  public override fun evaluationDao(): EvaluationDao = _evaluationDao.value

  public override fun userValueDao(): UserValueDao = _userValueDao.value

  public override fun exerciseRatingDao(): ExerciseRatingDao = _exerciseRatingDao.value

  public override fun exerciseReflectionDao(): ExerciseReflectionDao = _exerciseReflectionDao.value

  public override fun gratitudeDao(): GratitudeDao = _gratitudeDao.value
}
