package com.empiriact.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
        @Volatile
        internal var INSTANCE: EmpiriactDatabase? = null

        fun getDatabase(context: Context): EmpiriactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmpiriactDatabase::class.java,
                    "empiriact_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
