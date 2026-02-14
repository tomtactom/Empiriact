package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.R
import com.empiriact.app.ui.common.ContentDescriptions
import kotlinx.coroutines.launch

private val experimentSuggestions = listOf(
    "10 Minuten spazieren",
    "5 Minuten frische Luft am Fenster/Balkon",
    "Kurze Nachricht an eine vertraute Person",
    "15 Minuten ruhiges Aufräumen",
    "2 Minuten Atemfokus + Körperwahrnehmung"
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PsychoeducationActivationFoundationsScreen(navController: NavController) {
    var weekExperiment by remember { mutableStateOf("") }
    var reflectionStep by remember { mutableStateOf("") }
    var reflectionDirection by remember { mutableStateOf("") }
    var reflectionSupport by remember { mutableStateOf("") }

    val pageCount = 6
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gute Ausgangsbedingungen schaffen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        }
                    },
                    enabled = pagerState.currentPage > 0
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Vorherige Seite")
                }

                PageIndicator(currentPage = pagerState.currentPage, pageCount = pageCount)

                if (pagerState.currentPage < pageCount - 1) {
                    IconButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Nächste Seite")
                    }
                } else {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Fertig")
                    }
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            when (page) {
                0 -> ModulePage(title = "1) Worum es hier geht", illustrationResId = R.drawable.illustration_module_page_01) {
                    Text(
                        "Damit Veränderung im Alltag leichter starten kann, hilft ein klarer Rahmen: Du übernimmst eine aktive Rolle – die App unterstützt dich dabei wie ein Coach.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SpacerLine()
                    Text(
                        "Coach (Übersetzung/Erklärung): Begleitung, die hilft zu strukturieren, zu reflektieren und dranzubleiben – ohne zu bewerten.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Therapeutischer Fokus: Du bist nicht passiv betroffen, sondern aktiv lernend. Das stärkt Selbstwirksamkeit und verbessert langfristig die Chance auf stabile Veränderungen.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                1 -> ModulePage(title = "2) Kernidee: Aktiv werden als Lernprozess", illustrationResId = R.drawable.illustration_module_page_02) {
                    Text(
                        "Ein zentrales Element der werteorientierten Verhaltensaktivierung ist: Du planst kleine Aktivitäten und probierst sie zwischen deinen Check-ins aus.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Bullet("Werteorientiert heißt: Du richtest dich an dem aus, was dir wirklich wichtig ist (z. B. Nähe, Gesundheit, Freiheit, Lernen).")
                    Bullet("Es geht eher um Ausprobieren als um „perfekt machen“.")
                    Bullet("Wenn etwas nicht klappt, kann das trotzdem wertvolle Info liefern.")
                    SpacerLine()
                    Text("Wenn etwas nicht klappt, frage dich:", fontWeight = FontWeight.SemiBold)
                    Bullet("Was hat gebremst?")
                    Bullet("Was hat geholfen – auch ein bisschen?")
                    Bullet("Was passt (noch) nicht zu dir?")
                    SpacerLine()
                    Text(
                        "Fachbegriff: antidepressiv wirksam = etwas, das Stimmung, Antrieb und Stabilität langfristig unterstützen kann (bei jeder Person etwas anders).",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                2 -> ModulePage(title = "3) Was dich in den nächsten Wochen erwartet", illustrationResId = R.drawable.illustration_module_page_03) {
                    Text("Du lernst Schritt für Schritt:", style = MaterialTheme.typography.bodyMedium)
                    Bullet("depressive Stimmung und Antriebsverlust besser einzuordnen")
                    Bullet("zu erkennen, was dich stärkt (und was dich eher runterzieht)")
                    Bullet("deine Werte zu entdecken")
                    Bullet("Aktivitäten zu wählen, die diese Werte im Alltag wieder sichtbarer machen")
                    SpacerLine()
                    Text(
                        "Ergänzung aus Verhaltenstherapie: Kleine, wiederholte Schritte wirken meist stärker als seltene große Vorsätze. Deshalb zählt Kontinuität mehr als Intensität.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                3 -> ModulePage(title = "Kompakt-Screen (App-Version)", illustrationResId = R.drawable.illustration_module_page_04) {
                    Text(
                        "Du sammelst in den nächsten Wochen Erfahrungen damit, was dir hilft, dich stabiler zu fühlen. Dafür testest du kleine Aktivitäten im Alltag. Nicht alles fühlt sich sofort gut an – und auch das kann dir zeigen, was du brauchst und was besser zu dir passt.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SpacerLine()
                    Text(
                        "Merke: Erst handeln, dann bewerten. Positive Effekte bei Aktivierung entstehen oft zeitverzögert.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                4 -> ModulePage(title = "4) Mini-Übung: „Experiment statt Prüfung“ (2 Minuten)", illustrationResId = R.drawable.illustration_module_page_05) {
                    Text(
                        "Wähle für die nächste Woche eine kleine Sache, die du testen möchtest.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Wenn es schwerfällt: Starte mit der kleinstmöglichen Version (z. B. 2 Minuten statt 20).",
                        style = MaterialTheme.typography.bodySmall
                    )
                    experimentSuggestions.forEach { suggestion ->
                        AssistChip(
                            onClick = { weekExperiment = suggestion },
                            label = { Text(suggestion) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    OutlinedTextField(
                        value = weekExperiment,
                        onValueChange = { weekExperiment = it },
                        label = { Text("Mein Wochen-Experiment") },
                        supportingText = { Text("Halte es klein und realistisch (ca. 2–15 Minuten).") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                5 -> ModulePage(title = "5) Reflexionsfragen für die App", illustrationResId = R.drawable.illustration_module_page_06) {
                    OutlinedTextField(
                        value = reflectionStep,
                        onValueChange = { reflectionStep = it },
                        label = { Text("Was wäre ein kleiner Schritt, der zu mir passen könnte?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reflectionDirection,
                        onValueChange = { reflectionDirection = it },
                        label = { Text("Woran würde ich merken: „Das war in die richtige Richtung“ (auch wenn es nur 5% ist)?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reflectionSupport,
                        onValueChange = { reflectionSupport = it },
                        label = { Text("Welche Unterstützung (Zeit, Ort, Erinnerung, Person) könnte den Schritt erleichtern?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Abschließen")
                    }
                }
            }
        }
    }
}

@Composable
private fun ModulePage(
    title: String,
    illustrationResId: Int = R.drawable.illustration_module_page_01,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    AndroidModuleBanner()
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = illustrationResId),
                        contentDescription = "Illustration zu $title",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    content()
                }
            )
        }
    }
}

@Composable
private fun AndroidModuleBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "🤖", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Android Lernpfad",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Material Design, kleine Schritte, klare Struktur",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
private fun PageIndicator(currentPage: Int, pageCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
            )
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Text("• $text", style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun SpacerLine() {
    Divider(modifier = Modifier.padding(vertical = 2.dp))
}
