package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PageIndicator(currentPage = pagerState.currentPage, pageCount = pageCount)
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
                0 -> ModulePage(title = "Worum es hier geht", illustrationResId = R.drawable.illustration_module_page_01) {
                    Text(
                        "Damit Veränderung im Alltag leichter starten kann, hilft ein klarer Rahmen: Du übernimmst eine aktive Rolle – die App unterstützt dich dabei wie ein Coach.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    AffirmationBlock(
                        title = "Autonomie-Impuls",
                        text = "Ein möglicher Einstieg ist ein kleiner, selbstgewählter Schritt. Viele erleben das als entlastend, besonders an schweren Tagen."
                    )
                    SpacerLine()
                    Text(
                        "Coach (Übersetzung/Erklärung): Begleitung, die hilft zu strukturieren, zu reflektieren und dranzubleiben – ohne zu bewerten.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Therapeutischer Fokus: Jede eigene Beobachtung und jeder kleine Versuch fördert Selbstwirksamkeit und unterstützt langfristig stabile Veränderungen.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                1 -> ModulePage(title = "Kernidee: Aktiv werden als Lernprozess", illustrationResId = R.drawable.illustration_module_page_02) {
                    Text(
                        "Ein zentrales Element der werteorientierten Verhaltensaktivierung ist: Du planst kleine Aktivitäten und probierst sie zwischen deinen Check-ins aus.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Bullet("Werteorientiert heißt: Du richtest dich an dem aus, was dir wirklich wichtig ist (z. B. Nähe, Gesundheit, Freiheit, Lernen).")
                    Bullet("Der Fokus liegt auf Ausprobieren in kleinen, umsetzbaren Schritten.")
                    Bullet("Wenn etwas nicht klappt, kann das trotzdem wertvolle Info liefern.")
                    SpacerLine()
                    Text("Wenn etwas nicht klappt, frage dich:", fontWeight = FontWeight.SemiBold)
                    Bullet("Was hat gebremst?")
                    Bullet("Was hat geholfen – auch ein bisschen?")
                    Bullet("Was passt (noch) nicht zu dir?")
                    SpacerLine()
                    AffirmationBlock(
                        title = "Affirmative Haltung",
                        text = "Innehalten und Anpassen kann ein hilfreicher Teil des Lernprozesses sein. Viele berichten dabei mehr Selbststeuerung und einen freundlicheren Umgang mit sich selbst."
                    )
                    SpacerLine()
                    Text(
                        "Fachbegriff: antidepressiv wirksam = etwas, das Stimmung, Antrieb und Stabilität langfristig unterstützen kann (bei jeder Person etwas anders).",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                2 -> ModulePage(title = "Was dich in den nächsten Wochen erwartet", illustrationResId = R.drawable.illustration_module_page_03) {
                    Text("Du lernst Schritt für Schritt:", style = MaterialTheme.typography.bodyMedium)
                    Bullet("depressive Stimmung und Antriebsverlust besser einzuordnen")
                    Bullet("zu erkennen, was dich stärkt (und was dich eher runterzieht)")
                    Bullet("deine Werte zu entdecken")
                    Bullet("Aktivitäten zu wählen, die diese Werte im Alltag wieder sichtbarer machen")
                    SpacerLine()
                    Text(
                        "Ergänzung aus Verhaltenstherapie: Kleine, wiederholte Schritte entfalten häufig Wirkung. Kontinuität und passende Intensität können gemeinsam hilfreich sein.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    AffirmationBlock(
                        title = "CBT-Mikroschritt",
                        text = "Eine kurze Reflexion kann Orientierung geben: Situation → Gedanke → Gefühl → Handlung. Daraus ergeben sich oft passende nächste Mikroschritte."
                    )
                }

                3 -> ModulePage(title = "Kompakt-Screen (App-Version)", illustrationResId = R.drawable.illustration_module_page_04) {
                    Text(
                        "Du sammelst in den nächsten Wochen Erfahrungen damit, was dir hilft, dich stabiler zu fühlen. Dafür testest du kleine Aktivitäten im Alltag. Manche Effekte zeigen sich zeitversetzt und können Hinweise geben, was gut zu dir passt.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SpacerLine()
                    Text(
                        "Merke: Erst handeln, dann bewerten. Positive Effekte bei Aktivierung entstehen oft zeitverzögert.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    AffirmationBlock(
                        title = "Selbstunterstützung",
                        text = "Eine freundliche innere Sprache kann unterstützen, zum Beispiel so, wie bei einer vertrauten Person: klar, wohlwollend und ermutigend. Das fördert oft Motivation und emotionale Stabilität."
                    )
                }

                4 -> ModulePage(title = "Mini-Übung: „Experiment statt Prüfung“ (2 Minuten)", illustrationResId = R.drawable.illustration_module_page_05) {
                    Text(
                        "Wähle für die nächste Woche eine kleine Sache, die du testen möchtest.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Wenn es schwerfällt: Starte mit der kleinstmöglichen Version (z. B. 2 Minuten statt 20).",
                        style = MaterialTheme.typography.bodySmall
                    )
                    AffirmationBlock(
                        title = "Wahlmöglichkeiten",
                        text = "Hilfreich ist oft eine heute realistische Variante. Ein machbarer Schritt kann Vertrauen aufbauen; bei Bedarf lässt sich der Plan jederzeit vereinfachen oder anpassen."
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
                        supportingText = { Text("Halte es klein und realistisch (ca. 2–15 Minuten). Optional: eine Wenn-dann-Formulierung wie „Wenn es 18:00 ist, dann starte ich 5 Minuten.“") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                5 -> ModulePage(title = "Reflexionsfragen für die App", illustrationResId = R.drawable.illustration_module_page_06) {
                    AffirmationBlock(
                        title = "Bestärkung",
                        text = "Kurze, ehrliche Antworten sind oft gut nutzbar. Stimmigkeit im eigenen Alltag ist ein hilfreicher Maßstab für die nächsten Schritte."
                    )
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
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = illustrationResId),
                        contentDescription = "Illustration zu $title",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(148.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    content()
                }
            )
        }
    }
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
private fun AffirmationBlock(title: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(text, style = MaterialTheme.typography.bodySmall)
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
