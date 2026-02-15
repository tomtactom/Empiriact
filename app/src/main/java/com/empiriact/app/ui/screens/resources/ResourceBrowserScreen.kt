package com.empiriact.app.ui.screens.resources

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// ============== Datenmodelle für Resource Browser ==============

private data class PsychologicalResource(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val type: String, // "article", "exercise", "questionnaire"
    val estimatedTime: Int,
    val isFavorite: Boolean = false
)

private fun getPsychologicalResources(): List<PsychologicalResource> {
    return listOf(
        PsychologicalResource(
            id = "resource_1",
            title = "Die Physiologie der Angst",
            description = "Verstehe, wie dein Körper auf Bedrohungen reagiert und warum deine Symptome normal sind.",
            category = "Angststörungen",
            difficulty = "Anfänger",
            type = "article",
            estimatedTime = 5
        ),
        PsychologicalResource(
            id = "resource_2",
            title = "Atemkontrolle bei Panikattacken",
            description = "Lerne die 4-7-8 Atemtechnik, um dein Nervensystem zu beruhigen.",
            category = "Entspannung",
            difficulty = "Anfänger",
            type = "exercise",
            estimatedTime = 3
        ),
        PsychologicalResource(
            id = "resource_3",
            title = "Gedankenfallen erkennen",
            description = "Identifiziere häufige kognitive Verzerrungen und lerne, sie zu korrigieren.",
            category = "Kognitiv-Behavioral",
            difficulty = "Fortgeschrittene",
            type = "article",
            estimatedTime = 8
        ),
        PsychologicalResource(
            id = "resource_4",
            title = "Selbstmitgefühl-Übung",
            description = "Eine Meditation zur Entwicklung von Selbstmitgefühl statt Selbstkritik.",
            category = "Achtsamkeit",
            difficulty = "Anfänger",
            type = "exercise",
            estimatedTime = 10
        ),
        PsychologicalResource(
            id = "resource_5",
            title = "Schlafhygiene-Fragebogen",
            description = "Bewerte deine aktuellen Schlafgewohnheiten und erhalte personalisierte Tipps.",
            category = "Schlaf",
            difficulty = "Anfänger",
            type = "questionnaire",
            estimatedTime = 5
        ),
        PsychologicalResource(
            id = "resource_6",
            title = "Soziale Angst überwinden",
            description = "Expositions- und Kognitions-Techniken für soziale Angststörung.",
            category = "Angststörungen",
            difficulty = "Fortgeschrittene",
            type = "article",
            estimatedTime = 12
        ),
        PsychologicalResource(
            id = "resource_7",
            title = "Body Scan Meditation",
            description = "Eine geführte Meditation zur Erhöhung des Körperbewusstseins.",
            category = "Achtsamkeit",
            difficulty = "Anfänger",
            type = "exercise",
            estimatedTime = 15
        ),
        PsychologicalResource(
            id = "resource_8",
            title = "Depression verstehen",
            description = "Die biologischen, psychologischen und sozialen Faktoren der Depression.",
            category = "Depression",
            difficulty = "Anfänger",
            type = "article",
            estimatedTime = 10
        ),
        PsychologicalResource(
            id = "resource_9",
            title = "Aktivierungstagebuch",
            description = "Überwache und erhöhe deine Aktivität systematisch.",
            category = "Depression",
            difficulty = "Anfänger",
            type = "questionnaire",
            estimatedTime = 5
        ),
        PsychologicalResource(
            id = "resource_10",
            title = "ACT-Defusions-Übungen",
            description = "Akzeptanz- und Commitment-Therapie Techniken.",
            category = "Kognitiv-Behavioral",
            difficulty = "Fortgeschrittene",
            type = "exercise",
            estimatedTime = 8
        )
    )
}

// ============== Main Resource Browser ==============

@Composable
fun ResourceBrowserScreen(onBack: () -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedDifficulty by rememberSaveable { mutableStateOf<String?>(null) }
    var showFavoritesOnly by rememberSaveable { mutableStateOf(false) }
    var viewMode by rememberSaveable { mutableStateOf("grid") } // "grid" or "list"

    val resources = remember { getPsychologicalResources() }
    val categories = remember { resources.map { it.category }.distinct().sorted() }
    val difficulties = listOf("Anfänger", "Fortgeschrittene")

    val filteredResources = remember(searchQuery, selectedCategory, selectedDifficulty, showFavoritesOnly) {
        resources.filter { resource ->
            val matchesSearch = searchQuery.isEmpty() ||
                    resource.title.contains(searchQuery, ignoreCase = true) ||
                    resource.description.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null || resource.category == selectedCategory
            val matchesDifficulty = selectedDifficulty == null || resource.difficulty == selectedDifficulty

            matchesSearch && matchesCategory && matchesDifficulty
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6366F1).copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                }
                Text(
                    text = "Ressourcen-Bibliothek",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { viewMode = if (viewMode == "grid") "list" else "grid" }) {
                    Icon(
                        imageVector = if (viewMode == "grid") Icons.Default.ViewList else Icons.Default.ViewAgenda,
                        contentDescription = "Ansicht wechseln"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                placeholder = { Text("Suchen...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
        }

        // Filters
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        ) {
            if (categories.isNotEmpty()) {
                Text(
                    text = "Kategorien",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Alle") }
                    )
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = if (selectedCategory == category) null else category },
                            label = { Text(category) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Difficulty Filter
            Text(
                text = "Schwierigkeitsgrad",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDifficulty == null,
                    onClick = { selectedDifficulty = null },
                    label = { Text("Alle") }
                )
                difficulties.forEach { difficulty ->
                    FilterChip(
                        selected = selectedDifficulty == difficulty,
                        onClick = { selectedDifficulty = if (selectedDifficulty == difficulty) null else difficulty },
                        label = { Text(difficulty) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Results Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredResources.size} Ressourcen gefunden",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Content
        if (filteredResources.isEmpty()) {
            EmptyResourceState(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
            ) {
                items(filteredResources) { resource ->
                    ResourceCard(resource = resource)
                }
            }
        }
    }
}

@Composable
private fun ResourceCard(resource: PsychologicalResource) {
    var isFavorite by rememberSaveable { mutableStateOf(resource.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favorit",
                        tint = if (isFavorite) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Beschreibung
            Text(
                text = resource.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Metadaten
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type Badge
                val (typeIcon, typeColor, typeLabel) = when (resource.type) {
                    "article" -> Triple("📖", Color(0xFF6366F1), "Artikel")
                    "exercise" -> Triple("🏋️", Color(0xFF10B981), "Übung")
                    else -> Triple("📋", Color(0xFFF59E0B), "Fragebogen")
                }

                Box(
                    modifier = Modifier
                        .background(typeColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = typeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Difficulty
                Box(
                    modifier = Modifier
                        .background(
                            color = if (resource.difficulty == "Anfänger") Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = resource.difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (resource.difficulty == "Anfänger") Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Zeit
                Text(
                    text = "⏱ ${resource.estimatedTime} min",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Category
            Text(
                text = resource.category,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6366F1),
                fontWeight = FontWeight.SemiBold
            )

            // CTA
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Öffnen")
            }
        }
    }
}

@Composable
private fun EmptyResourceState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF6366F1).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📚",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Text(
                text = "Keine Ressourcen gefunden",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Versuche, deine Filter zu ändern oder einen anderen Suchbegriff zu nutzen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============== Insights Screen für Lernpfad ==============

@Composable
fun LearningPathScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
            }
            Text(
                text = "Dein Lernpfad",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Progress Overview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF6366F1).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gesamtfortschritt",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "6 von 12 Module abgeschlossen",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1)
                    )
                }

                androidx.compose.material3.LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF6366F1)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Learning Paths
        Text(
            text = "Empfohlene Lernpfade",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = "Angstabbau 101",
            description = "Von Grundlagen bis zu praktischen Bewältigungsstrategien",
            progress = 75,
            modulesCompleted = 3,
            modulesTotal = 4,
            color = Color(0xFFF59E0B)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = "Emotionale Bewältigung",
            description = "Emotionen verstehen und regulieren",
            progress = 40,
            modulesCompleted = 2,
            modulesTotal = 5,
            color = Color(0xFF6366F1)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = "Wertorientiertes Leben",
            description = "Deine Werte definieren und leben",
            progress = 20,
            modulesCompleted = 1,
            modulesTotal = 5,
            color = Color(0xFFEC4899)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Recommended Next Steps
        Text(
            text = "Nächste Schritte",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        NextStepCard(
            title = "Kognitive Umstrukturierung",
            description = "Lerne, wie deine Gedanken deine Emotionen beeinflussen",
            duration = "8 min",
            difficulty = "Fortgeschrittene",
            icon = "🧠"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NextStepCard(
            title = "ACT-Übung: Werteorientierung",
            description = "Verbinde deine Handlungen mit deinen Werten",
            duration = "10 min",
            difficulty = "Anfänger",
            icon = "❤️"
        )
    }
}

@Composable
private fun LearningPathCard(
    title: String,
    description: String,
    progress: Int,
    modulesCompleted: Int,
    modulesTotal: Int,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color, CircleShape)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .width(150.dp)
                            .height(6.dp),
                        color = color
                    )
                    Text(
                        text = "$progress% abgeschlossen",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$modulesCompleted/$modulesTotal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun NextStepCard(
    title: String,
    description: String,
    duration: String,
    difficulty: String,
    icon: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { },
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
            Text(text = icon, style = MaterialTheme.typography.displaySmall)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(text = "•", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (difficulty == "Anfänger") Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

