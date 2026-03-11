package com.empiriact.app.services

import com.empiriact.app.BuildConfig
import com.empiriact.app.data.db.ActivityLogEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataDonationService(
    private val client: HttpClient = createDefaultClient(),
    private val donationUrl: String = BuildConfig.DATA_DONATION_URL
) {

    suspend fun donate(logs: List<ActivityLogEntity>): Boolean {
        val parsedUrl = donationUrl.toValidatedDonationUrl() ?: return false

        return try {
            val anonymizedLogs = anonymizeLogs(logs)
            val response = client.post(parsedUrl) {
                contentType(ContentType.Application.Json)
                setBody(anonymizedLogs)
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    internal fun anonymizeLogs(logs: List<ActivityLogEntity>): List<AnonymizedActivityLog> {
        return logs.map { log ->
            AnonymizedActivityLog(
                weekday = log.localDate.toWeekday(),
                hour = log.hour,
                valence = log.valence,
                textFingerprint = sha256(log.activityText),
                updatedAtDayBucket = log.updatedAtEpochMs / MILLIS_PER_DAY
            )
        }
    }

    private fun Int.toWeekday(): Int {
        val date = LocalDate.parse(this.toString(), DATE_FORMAT)
        return date.dayOfWeek.value
    }

    private fun sha256(value: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(value.trim().lowercase().toByteArray())
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    private fun String.toValidatedDonationUrl(): String? {
        val normalizedUrl = trim()
        if (normalizedUrl.isBlank()) {
            return null
        }

        val parsedUrl = runCatching { Url(normalizedUrl) }.getOrNull() ?: return null
        val isHttpProtocol = parsedUrl.protocol == URLProtocol.HTTP || parsedUrl.protocol == URLProtocol.HTTPS

        return normalizedUrl.takeIf { isHttpProtocol && parsedUrl.host.isNotBlank() }
    }

    @Serializable
    data class AnonymizedActivityLog(
        val weekday: Int,
        val hour: Int,
        val valence: Int,
        val textFingerprint: String,
        val updatedAtDayBucket: Long
    )

    private companion object {
        fun createDefaultClient(): HttpClient {
            return HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }

        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE
        const val MILLIS_PER_DAY = 86_400_000L
    }
}
