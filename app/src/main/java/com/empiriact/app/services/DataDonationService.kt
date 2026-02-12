package com.empiriact.app.services

import com.empiriact.app.data.db.ActivityLogEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class DataDonationService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun donate(logs: List<ActivityLogEntity>): Boolean {
        return try {
            // TODO: Anonymize logs before sending them
            val response = client.post("YOUR_SERVER_URL") {
                contentType(ContentType.Application.Json)
                setBody(logs)
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
