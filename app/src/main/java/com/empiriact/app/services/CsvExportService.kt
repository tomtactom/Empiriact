package com.empiriact.app.services

import com.empiriact.app.data.db.ActivityLogEntity
import java.io.File
import java.io.FileWriter
import java.time.LocalDate

class CsvExportService {

    fun export(logs: List<ActivityLogEntity>, file: File): Boolean {
        return try {
            FileWriter(file).use { writer ->
                writer.append("Date,Hour,Activity,Valence\n")
                for (log in logs) {
                    val date = dateFromYyyyMmDd(log.localDate)
                    writer.append("$date,${log.hour},${log.activityText},${log.valence}\n")
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun dateFromYyyyMmDd(dateInt: Int): String {
        val year = dateInt / 10000
        val month = (dateInt % 10000) / 100
        val day = dateInt % 100
        return String.format("%04d-%02d-%02d", year, month, day)
    }
}

