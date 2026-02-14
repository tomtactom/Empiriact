package com.empiriact.app

import android.app.Application
import com.empiriact.app.data.db.EmpiriactDatabase
import com.empiriact.app.data.repo.ActivityLogRepository
import com.empiriact.app.data.CheckinRepository
import com.empiriact.app.data.ExerciseRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.repo.GratitudeRepository
import com.empiriact.app.services.JsonExportService
import com.empiriact.app.ui.common.ViewModelFactory

class EmpiriactApplication : Application() {

    private val database by lazy { EmpiriactDatabase.getDatabase(this) }

    val activityLogRepository by lazy { ActivityLogRepository(database.activityLogDao()) }
    val exerciseRepository by lazy { ExerciseRepository(database.exerciseRatingDao()) }
    val gratitudeRepository by lazy { GratitudeRepository(database.gratitudeDao()) }
    val checkinRepository by lazy { CheckinRepository() }
    val settingsRepository by lazy { SettingsRepository(this) }

    private val jsonExportService by lazy { JsonExportService() }

    val viewModelFactory by lazy {
        ViewModelFactory(
            activityLogRepository = activityLogRepository,
            exerciseRepository = exerciseRepository,
            exerciseRatingDao = database.exerciseRatingDao(),
            gratitudeRepository = gratitudeRepository,
            jsonExportService = jsonExportService,
            settingsRepository = settingsRepository
        )
    }
}
