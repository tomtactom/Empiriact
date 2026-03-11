package com.empiriact.app.data.db

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.empiriact.app.data.Routine
import com.empiriact.app.data.Resource
import com.empiriact.app.data.Evaluation
import com.empiriact.app.data.UserValue

@Database(
    entities = [ActivityLogEntity::class, Routine::class, Resource::class, Evaluation::class, UserValue::class, ExerciseRatingEntity::class, ExerciseReflectionEntity::class, GratitudeEntity::class, PsychoeducationalModuleEntity::class, PassiveMarkerHourlyEntity::class],
    version = 15,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EmpiriactDatabase : RoomDatabase() {

    abstract fun activityLogDao(): ActivityLogDao
    abstract fun routineDao(): RoutineDao
    abstract fun resourceDao(): ResourceDao
    abstract fun evaluationDao(): EvaluationDao
    abstract fun userValueDao(): UserValueDao
    abstract fun exerciseRatingDao(): ExerciseRatingDao
    abstract fun exerciseReflectionDao(): ExerciseReflectionDao
    abstract fun gratitudeDao(): GratitudeDao
    abstract fun psychoeducationalModuleDao(): PsychoeducationalModuleDao
    abstract fun passiveMarkerDao(): PassiveMarkerDao

    companion object {
        internal val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `exercise_reflections` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `exerciseId` TEXT NOT NULL,
                        `reflection` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        internal val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `gratitude_logs` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `date` INTEGER NOT NULL,
                        `entry` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        internal val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `psychoeducational_modules` (
                        `moduleId` TEXT PRIMARY KEY NOT NULL,
                        `moduleTitle` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `isCompleted` INTEGER NOT NULL DEFAULT 0,
                        `rating` INTEGER,
                        `feedback` TEXT,
                        `completedAt` INTEGER,
                        `startedAt` INTEGER NOT NULL,
                        `isBookmarked` INTEGER NOT NULL DEFAULT 0,
                        `isExample` INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent()
                )
            }
        }

        internal val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `activity_logs` ADD COLUMN `peopleText` TEXT NOT NULL DEFAULT ''")
            }
        }

        internal val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `activity_logs` ADD COLUMN `durationMinutes` INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE `activity_logs` ADD COLUMN `difficultyRating` INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE `activity_logs` ADD COLUMN `activationLatencyMinutes` INTEGER DEFAULT NULL")
            }
        }


        internal val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `passive_marker_hourly` (
                        `key` TEXT NOT NULL,
                        `localDate` INTEGER NOT NULL,
                        `hour` INTEGER NOT NULL,
                        `stepCount` INTEGER,
                        `sleepDurationMinutesPreviousNight` INTEGER,
                        `screenTimeMinutesInHour` INTEGER,
                        `updatedAtEpochMs` INTEGER NOT NULL,
                        PRIMARY KEY(`key`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_passive_marker_hourly_localDate_hour`
                    ON `passive_marker_hourly` (`localDate`, `hour`)
                    """.trimIndent()
                )
            }
        }


        internal val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `passive_marker_hourly` ADD COLUMN `isEstimated` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        @Volatile
        internal var INSTANCE: EmpiriactDatabase? = null

        fun getDatabase(context: Context): EmpiriactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmpiriactDatabase::class.java,
                    "empiriact_database"
                )
                     .addMigrations(MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15)
                    .apply {
                        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
                        if (isDebuggable) {
                            fallbackToDestructiveMigration(dropAllTables = true)
                        }
                    }
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
