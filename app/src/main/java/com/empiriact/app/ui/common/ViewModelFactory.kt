package com.empiriact.app.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.empiriact.app.data.repo.ActivityLogRepository
import com.empiriact.app.data.ExerciseRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ExerciseRatingDao
import com.empiriact.app.data.repo.GratitudeRepository
import com.empiriact.app.data.repo.PassiveMarkerRepository
import com.empiriact.app.services.JsonExportService
import com.empiriact.app.services.PassiveSnapshotRefresher
import com.empiriact.app.ui.screens.overview.OverviewViewModel
import com.empiriact.app.ui.screens.rating.ExerciseRatingViewModel
import com.empiriact.app.ui.screens.resources.gratitude.GratitudeViewModel
import com.empiriact.app.ui.screens.settings.JsonExportViewModel
import com.empiriact.app.ui.screens.today.TodayViewModel

class ViewModelFactory(
    private val activityLogRepository: ActivityLogRepository,
    private val exerciseRepository: ExerciseRepository,
    private val exerciseRatingDao: ExerciseRatingDao,
    private val gratitudeRepository: GratitudeRepository,
    private val jsonExportService: JsonExportService,
    private val settingsRepository: SettingsRepository,
    private val passiveMarkerRepository: PassiveMarkerRepository,
    private val passiveSnapshotRefresher: PassiveSnapshotRefresher
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodayViewModel(activityLogRepository, settingsRepository, passiveSnapshotRefresher) as T
        }
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OverviewViewModel(activityLogRepository, exerciseRepository, passiveMarkerRepository, settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(GratitudeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GratitudeViewModel(gratitudeRepository) as T
        }
        if (modelClass.isAssignableFrom(JsonExportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JsonExportViewModel(activityLogRepository, jsonExportService) as T
        }
        if (modelClass.isAssignableFrom(ExerciseRatingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseRatingViewModel(exerciseRatingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
