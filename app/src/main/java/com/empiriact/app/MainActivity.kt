package com.empiriact.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as EmpiriactApplication
            Surface(modifier = Modifier.fillMaxSize()) {
                EmpiriactApp(application.viewModelFactory, application.settingsRepository)
            }
        }
    }
}
