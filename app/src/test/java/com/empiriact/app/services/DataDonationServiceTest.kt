package com.empiriact.app.services

import com.empiriact.app.data.db.ActivityLogEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DataDonationServiceTest {

    private val service = DataDonationService()

    @Test
    fun anonymizeLogs_transformsSensitiveFields() {
        val logs = listOf(
            ActivityLogEntity(
                key = "20250213-9",
                localDate = 20250213,
                hour = 9,
                activityText = "Morning run with partner",
                valence = 2,
                updatedAtEpochMs = 1_739_404_800_000
            )
        )

        val anonymized = service.anonymizeLogs(logs)

        assertEquals(1, anonymized.size)
        assertEquals(4, anonymized.first().weekday) // 2025-02-13 = Thursday
        assertEquals(9, anonymized.first().hour)
        assertEquals(2, anonymized.first().valence)
        assertEquals(20_131, anonymized.first().updatedAtDayBucket)
        assertNotEquals("Morning run with partner", anonymized.first().textFingerprint)
        assertEquals(64, anonymized.first().textFingerprint.length)
    }

    @Test
    fun anonymizeLogs_normalizesTextBeforeHashing() {
        val baseLog = ActivityLogEntity(
            key = "20250213-10",
            localDate = 20250213,
            hour = 10,
            activityText = "Evening Walk",
            valence = 1,
            updatedAtEpochMs = 1_739_404_800_000
        )

        val variantLog = baseLog.copy(
            key = "20250213-11",
            hour = 11,
            activityText = "  evening walk  "
        )

        val anonymized = service.anonymizeLogs(listOf(baseLog, variantLog))

        assertEquals(anonymized[0].textFingerprint, anonymized[1].textFingerprint)
    }
}
