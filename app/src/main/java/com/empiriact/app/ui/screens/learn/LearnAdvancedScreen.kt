package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions

data class AdvancedTechnique(val title: String, val content: String)

private val techniques = listOf(
    AdvancedTechnique(
        "Tiefe Reflexion",
        "Lerne, wie du deine Gedanken und Gefühle auf einer tieferen Ebene analysieren kannst. Dies führt zu besseren Einsichten und Entscheidungen."
    ),
    AdvancedTechnique(
        "Verhaltensoptimierung",
        "Entdecke fortgeschrittene Strategien zur Optimierung deiner täglichen Gewohnheiten und zum Erreichen von Zielen."
    ),
    AdvancedTechnique(
        "Mentales Training",
        "Entwickle mentale Techniken, um deine Konzentration, Ausdauer und emotionale Widerstandskraft zu verbessern."
    ),
    AdvancedTechnique(
        "Datengestützte Entscheidungen",
        "Nutze die Insights aus deinen Daten, um fundierte Entscheidungen zu treffen und deinen Fortschritt zu maximieren."
    ),
    AdvancedTechnique(
        "Systemisches Denken",
        "Verstehe die Zusammenhänge zwischen verschiedenen Aspekten deines Lebens und wie sie sich gegenseitig beeinflussen."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnAdvancedScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fortgeschrittene Techniken") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ContentDescriptions.BACK_BUTTON)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(techniques.size) { index ->
                val technique = techniques[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = technique.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = technique.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
