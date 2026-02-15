package com.empiriact.app.data.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class EmpiriactDatabaseMigrationTest {

    @get:Rule
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        EmpiriactDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate8To9_createsExerciseReflectionsTable() {
        migrationTestHelper.createDatabase(DB_NAME, 8).close()

        migrationTestHelper.runMigrationsAndValidate(
            DB_NAME,
            9,
            true,
            EmpiriactDatabase.MIGRATION_8_9
        )

        migrationTestHelper.openDatabase(DB_NAME).use { db ->
            db.query("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='exercise_reflections'").use { cursor ->
                cursor.moveToFirst()
                assertEquals(1, cursor.getInt(0))
            }
        }
    }

    @Test
    fun migrate9To10_createsGratitudeLogsTable() {
        migrationTestHelper.createDatabase(DB_NAME, 9).close()

        migrationTestHelper.runMigrationsAndValidate(
            DB_NAME,
            10,
            true,
            EmpiriactDatabase.MIGRATION_9_10
        )

        migrationTestHelper.openDatabase(DB_NAME).use { db ->
            db.query("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='gratitude_logs'").use { cursor ->
                cursor.moveToFirst()
                assertEquals(1, cursor.getInt(0))
            }
        }
    }

    private companion object {
        const val DB_NAME = "empiriact-migration-test"
    }
}
