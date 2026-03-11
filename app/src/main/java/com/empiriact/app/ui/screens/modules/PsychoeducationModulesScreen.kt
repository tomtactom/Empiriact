package com.empiriact.app.ui.screens.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route

// ============== Datenklassen ==============

data class ModuleItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val route: String,
    val estimatedTime: String
)

data class ModuleSection(
    val sectionTitle: String,
    val sectionDescription: String,
    val sectionModules: List<ModuleItem>
)

// ============== Hauptscreenkomponente ==============

@Composable
fun PsychoeducationModulesScreen(navController: NavController) {
    val psychoeducationModules = listOf(
        ModuleItem(
            title = "Grübeln & Rumination",
            description = "Ein umfassendes Modul über Grübeln, seine Folgen und Regulationsmöglichkeiten",
            icon = Icons.Default.School,
            color = Color(0xFF6366F1),
            route = Route.GruebelnModule.route,
            estimatedTime = "~45 min"
        ),
        ModuleItem(
            title = "Denkstile verstehen",
            description = "Unterscheide zwischen ungünstigen und günstigen Denkmustern",
            icon = Icons.Default.School,
            color = Color(0xFF8B5CF6),
            route = Route.DenkstileModule.route,
            estimatedTime = "~20 min"
        ),
        ModuleItem(
            title = "RND verstehen & regulieren",
            description = "Repetitives negatives Denken erkennen und transformieren",
            icon = Icons.Default.School,
            color = Color(0xFF3B82F6),
            route = Route.RNDModule.route,
            estimatedTime = "~35 min"
        )
    )

    val interactiveExercises = listOf(
        ModuleItem(
            title = "5-4-3-2-1 Erdungsübung",
            description = "Eine schnelle Technik, um aus Gedankenschleifen in die Gegenwart zu kommen",
            icon = Icons.Default.FitnessCenter,
            color = Color(0xFF3B82F6),
            route = "interactive_exercises",
            estimatedTime = "~5 min"
        ),
        ModuleItem(
            title = "Progressive Muskelentspannung",
            description = "Spanne und entspanne Muskelgruppen systematisch",
            icon = Icons.Default.FitnessCenter,
            color = Color(0xFF8B5CF6),
            route = "interactive_exercises",
            estimatedTime = "~10 min"
        ),
        ModuleItem(
            title = "Gedanken-Etikettierung",
            description = "Lerne, Gedanken als mentale Ereignisse zu behandeln",
            icon = Icons.Default.FitnessCenter,
            color = Color(0xFF10B981),
            route = "interactive_exercises",
            estimatedTime = "~7 min"
        ),
        ModuleItem(
            title = "Roten Faden finden",
            description = "Erlebnis-Übung mit Wolle & räumlichen Metaphern",
            icon = Icons.Default.FitnessCenter,
            color = Color(0xFFFF6B9D),
            route = "interactive_exercises",
            estimatedTime = "~25 min"
        ),
        ModuleItem(
            title = "3-Fragen-Daumenregel",
            description = "Gedankenexperiment: Warum vs. Wie-Fragen",
            icon = Icons.Default.FitnessCenter,
            color = Color(0xFFFFA366),
            route = "interactive_exercises",
            estimatedTime = "~20 min"
        )
    )

    val otherModules = listOf(
        ModuleItem(
            title = "Ressourcen-Bibliothek",
            description = "10+ psychologische Ressourcen mit Filter & Suche",
            icon = Icons.Default.AutoStories,
            color = Color(0xFFF59E0B),
            route = "resource_browser",
            estimatedTime = "Variabel"
        ),
        ModuleItem(
            title = "Lernpfade",
            description = "Strukturierte Lernwege mit Fortschritt-Tracking",
            icon = Icons.Default.Info,
            color = Color(0xFFEC4899),
            route = "learning_path",
            estimatedTime = "Selbstbestimmt"
        )
    )

    val sections = listOf(
        ModuleSection(
            sectionTitle = "Psychoedukation",
            sectionDescription = "Wissenschaftlich fundierte Lernmodule zur Psychoeduktion",
            sectionModules = psychoeducationModules
        ),
        ModuleSection(
            sectionTitle = "Interaktive Übungen",
            sectionDescription = "Praktische Übungen zum Trainieren und Anwenden",
            sectionModules = interactiveExercises
        ),
        ModuleSection(
            sectionTitle = "Weitere Inhalte",
            sectionDescription = "Zusätzliche Ressourcen und Lernwege",
            sectionModules = otherModules
        )
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
            }
            Text(
                text = "Module",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.size(40.dp))
        }

        // Intro Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Psychoedukative Module",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Wähle ein Modul, um zu starten. Jedes Modul ist wissenschaftlich fundiert und praktisch anwendbar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modules List mit Sektionen
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
        ) {
            items(sections.size) { sectionIndex ->
                val section = sections[sectionIndex]
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Section Header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = section.sectionTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = section.sectionDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Module Cards in dieser Sektion
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (module in section.sectionModules) {
                            ModuleCard(
                                module = module,
                                onClick = {
                                    navController.navigate(module.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(module: ModuleItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Farbiger Icon-Kreis
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = module.color.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = module.icon,
                    contentDescription = null,
                    tint = module.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Inhalte
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "⏱ ${module.estimatedTime}",
                    style = MaterialTheme.typography.labelSmall,
                    color = module.color,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

