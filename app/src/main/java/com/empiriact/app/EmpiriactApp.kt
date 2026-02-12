package com.empiriact.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.navigation.EmpiriactNavGraph

@Composable
fun EmpiriactApp(factory: ViewModelFactory) {
    MaterialTheme {
        EmpiriactNavGraph(factory)
    }
}
