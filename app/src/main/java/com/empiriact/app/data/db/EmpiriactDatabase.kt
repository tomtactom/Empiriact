package com.empiriact.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.empiriact.app.BuildConfig
import com.empiriact.app.data.Routine
import com.empiriact.app.data.Resource
import com.empiriact.app.data.Evaluation
import com.empiriact.app.data.UserValue

@Database(
    entities = [ActivityLogEntity::class, Routine::class, Resource::class, Evaluation::class, UserValue::class, ExerciseRatingEntity::class, ExerciseReflectionEntity::class, GratitudeEntity::class],
    version = 10,
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

        @Volatile
        internal var INSTANCE: EmpiriactDatabase? = null

        fun getDatabase(context: Context): EmpiriactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmpiriactDatabase::class.java,
                    "empiriact_database"
                )
                    .addMigrations(MIGRATION_8_9, MIGRATION_9_10)
                    .apply {
                        if (BuildConfig.DEBUG) {
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
