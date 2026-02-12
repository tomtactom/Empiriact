package com.empiriact.app.ui.screens.settings

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.services.CsvExportService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class CsvExportViewModel(private val repository: ActivityLogRepository) : ViewModel() {

    fun exportCsv(context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val logs = repository.getAll().first()
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "empiriact_export.csv")
            val success = CsvExportService().export(logs, file)
            onResult(success)
        }
    }
}
