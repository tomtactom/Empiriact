package com.empiriact.app.services

import com.empiriact.app.data.db.ActivityLogEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonExportService {

    private val json = Json { prettyPrint = true }

    fun export(logs: List<ActivityLogEntity>): String {
        return json.encodeToString(logs)
    }
}
