
package com.empiriact.app.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EmpiriactDatabaseTest {

    private lateinit var db: EmpiriactDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EmpiriactDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun daosAreAvailable() {
        assertNotNull(db.activityLogDao())
        assertNotNull(db.routineDao())
        assertNotNull(db.resourceDao())
        assertNotNull(db.evaluationDao())
        assertNotNull(db.userValueDao())
        assertNotNull(db.exerciseRatingDao())
        assertNotNull(db.exerciseReflectionDao())
    }
}
