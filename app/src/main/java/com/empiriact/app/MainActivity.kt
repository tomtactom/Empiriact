package com.empiriact.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var empiriactApplication: EmpiriactApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        empiriactApplication = application as EmpiriactApplication
        setContent {
            val application = empiriactApplication
            Surface(modifier = Modifier.fillMaxSize()) {
                EmpiriactApp(application.viewModelFactory, application.settingsRepository)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            empiriactApplication.passiveSnapshotRefresher.refreshSnapshot()
        }
    }
}
