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

data class BasicConcept(val title: String, val content: String)

private val concepts = listOf(
    BasicConcept(
        "Was ist Empiriact?",
        "Empiriact ist eine App, die dir hilft, empirische Prinzipien in deinen Alltag zu integrieren und deine persönliche Entwicklung zu fördern."
    ),
    BasicConcept(
        "Grundprinzipien",
        "Die Grundprinzipien basieren auf wissenschaftlichen Erkenntnissen und bewährten Praktiken. Sie helfen dir, fundierte Entscheidungen zu treffen."
    ),
    BasicConcept(
        "Wie man beginnt",
        "Starten Sie mit einer einfachen Gewohnheit. Verfolgen Sie Ihre Fortschritte und steigern Sie die Komplexität schrittweise."
    ),
    BasicConcept(
        "Häufige Missverständnisse",
        "Ein häufiges Missverständnis ist, dass Veränderung schnell passieren muss. Tatsächlich sind kleine, konsistente Schritte wirksamer."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnBasicsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grundlagen") },
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
            items(concepts.size) { index ->
                val concept = concepts[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = concept.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = concept.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
