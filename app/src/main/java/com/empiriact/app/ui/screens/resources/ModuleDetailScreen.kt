package com.empiriact.app.ui.screens.resources

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.empiriact.app.data.model.Course
import com.empiriact.app.data.model.Module
import kotlinx.serialization.json.Json

@Composable
fun ModuleDetailScreen(moduleId: String) {
    val context = LocalContext.current
    val decodedId = Uri.decode(moduleId)
    val module by produceState<Module?>(initialValue = null, context, decodedId) {
        value = loadEducationModules(context).firstOrNull { it.id == decodedId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (module == null) {
            Text(
                text = "Modul nicht gefunden",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Dieses Edukationsmodul konnte nicht geladen werden.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text(
                text = module?.title.orEmpty(),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = module?.description.orEmpty(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun loadEducationModules(context: android.content.Context): List<Module> {
    val json = Json { ignoreUnknownKeys = true }
    val candidateFiles = listOf("courses/gruebeln_foundation.json", "course.json")

    for (file in candidateFiles) {
        val modules = runCatching {
            context.assets.open(file).bufferedReader().use { reader ->
                val course = json.decodeFromString<Course>(reader.readText())
                course.modules
            }
        }.getOrNull()

        if (!modules.isNullOrEmpty()) {
            return modules
        }
    }

    return emptyList()
}
