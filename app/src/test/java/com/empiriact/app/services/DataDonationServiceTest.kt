package com.empiriact.app.services

import com.empiriact.app.data.db.ActivityLogEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DataDonationServiceTest {

    @Test
    fun anonymizeLogs_transformsSensitiveFields() {
        val service = testableService()
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
        val service = testableService()
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

    @Test
    fun donate_returnsFalseAndSkipsRequest_whenUrlIsBlank() = runTest {
        var requestCount = 0
        val service = DataDonationService(
            client = mockClient {
                requestCount++
                respondOk()
            },
            donationUrl = "   "
        )

        val result = service.donate(listOf(sampleLog()))

        assertFalse(result)
        assertEquals(0, requestCount)
    }

    @Test
    fun donate_usesConfiguredUrlPath_whenUrlIsValid() = runTest {
        var capturedRequest: HttpRequestData? = null
        val targetUrl = "https://example.org/data-donation"
        val service = DataDonationService(
            client = mockClient { request ->
                capturedRequest = request
                respondOk()
            },
            donationUrl = targetUrl
        )

        val result = service.donate(listOf(sampleLog()))

        assertTrue(result)
        assertEquals("/data-donation", capturedRequest?.url?.encodedPath)
        assertEquals("example.org", capturedRequest?.url?.host)
    }

    @Test
    fun donate_returnsFalseWithoutCrash_whenClientThrows() = runTest {
        val service = DataDonationService(
            client = mockClient { throw IllegalStateException("network down") },
            donationUrl = "https://example.org/data-donation"
        )

        val result = service.donate(listOf(sampleLog()))

        assertFalse(result)
    }


    private fun testableService(): DataDonationService {
        return DataDonationService(
            client = mockClient { respondOk() },
            donationUrl = "https://example.org/data-donation"
        )
    }

    private fun mockClient(handler: suspend (HttpRequestData) -> io.ktor.client.engine.mock.MockHttpResponseData): HttpClient {
        return HttpClient(MockEngine { request -> handler(request) }) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    private fun respondOk() = respond(
        content = "{}",
        status = HttpStatusCode.OK,
        headers = headersOf("Content-Type", ContentType.Application.Json.toString())
    )

    private fun sampleLog() = ActivityLogEntity(
        key = "20250213-12",
        localDate = 20250213,
        hour = 12,
        activityText = "Lunch",
        valence = 1,
        updatedAtEpochMs = 1_739_404_800_000
    )
}
