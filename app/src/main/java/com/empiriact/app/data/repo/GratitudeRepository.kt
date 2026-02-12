package com.empiriact.app.data.repo

import com.empiriact.app.data.db.GratitudeDao
import com.empiriact.app.data.db.GratitudeEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GratitudeRepository(private val gratitudeDao: GratitudeDao) {

    fun getGratitudeLogForDate(date: LocalDate): Flow<GratitudeEntity?> {
        return gratitudeDao.getGratitudeLogForDate(date)
    }

    suspend fun upsertGratitudeLog(entry: String, date: LocalDate) {
        val log = GratitudeEntity(date = date, entry = entry)
        gratitudeDao.upsert(log)
    }
}
