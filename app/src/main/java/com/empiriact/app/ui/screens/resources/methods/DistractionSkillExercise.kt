package com.empiriact.app.ui.screens.resources.methods

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.navigation.Route

private data class ActivitySuggestion(val name: String, val category: String)

private val activitySuggestions = listOf(
    // Für den Kopf (kognitive Ablenkung)
    ActivitySuggestion("Zähle von 100 in 7er-Schritten rückwärts", "Für den Kopf"),
    ActivitySuggestion("Nenne 5 Länder und ihre Hauptstädte", "Für den Kopf"),
    ActivitySuggestion("Plane im Kopf deinen nächsten Einkauf", "Für den Kopf"),
    ActivitySuggestion("Sage das Alphabet rückwärts auf", "Für den Kopf"),
    ActivitySuggestion("Lerne 5 neue Wörter in einer fremden Sprache", "Für den Kopf"),
    ActivitySuggestion("Beschreibe einen komplexen Gegenstand im Detail", "Für den Kopf"),
    ActivitySuggestion("Löse ein kleines Rätsel (z.B. Sudoku)", "Für den Kopf"),
    ActivitySuggestion("Denke an ein Tier für jeden Buchstaben des Alphabets", "Für den Kopf"),
    ActivitySuggestion("Stelle dir einen sicheren, angenehmen Ort vor", "Für den Kopf"),
    ActivitySuggestion("Nenne 5 Dinge, die du siehst und die blau sind", "Für den Kopf"),
    ActivitySuggestion("Erinnere dich an den Text deines Lieblingsliedes", "Für den Kopf"),
    ActivitySuggestion("Gehe im Kopf den Weg zu einem bekannten Ort ab", "Für den Kopf"),
    ActivitySuggestion("Nenne ein Land für jeden Buchstaben von A bis Z", "Für den Kopf"),
    ActivitySuggestion("Denke über die Handlung deines Lieblingsfilms nach", "Für den Kopf"),
    ActivitySuggestion("Liste alle Möbelstücke im Raum auf", "Für den Kopf"),
    ActivitySuggestion("Finde 5 Wörter, die sich auf 'Haus' reimen", "Für den Kopf"),
    ActivitySuggestion("Buchstabiere deinen Namen und deine Adresse rückwärts", "Für den Kopf"),
    ActivitySuggestion("Erfinde eine kurze Geschichte", "Für den Kopf"),
    ActivitySuggestion("Zähle die Ecken in dem Raum, in dem du dich befindest", "Für den Kopf"),
    ActivitySuggestion("Erinnere dich an ein schönes Gespräch mit einem Freund", "Für den Kopf"),
    ActivitySuggestion("Denk dir ein neues Rezept aus", "Für den Kopf"),
    ActivitySuggestion("Liste 10 Dinge auf, für die du dankbar bist", "Für den Kopf"),
    ActivitySuggestion("Nenne 7 berühmte Personen mit dem Buchstaben 'M'", "Für den Kopf"),
    ActivitySuggestion("Beschreibe den Aufbau einer Blume im Detail", "Für den Kopf"),
    ActivitySuggestion("Denke an 3 Witze, die du kennst", "Für den Kopf"),

    // Für die Sinne (sensorische Ablenkung)
    ActivitySuggestion("Höre bewusst ein Lied und achte auf die Instrumente", "Für die Sinne"),
    ActivitySuggestion("Iss etwas und beschreibe den Geschmack und die Textur", "Für die Sinne"),
    ActivitySuggestion("Fühle die Textur von drei verschiedenen Oberflächen", "Für die Sinne"),
    ActivitySuggestion("Rieche an einer Tasse Kaffee, Tee oder einem Gewürz", "Für die Sinne"),
    ActivitySuggestion("Halte einen Eiswürfel in der Hand und spüre die Kälte", "Für die Sinne"),
    ActivitySuggestion("Konzentriere dich 2 Minuten nur auf deine Atmung", "Für die Sinne"),
    ActivitySuggestion("Schau aus dem Fenster und finde etwas, das du noch nie bemerkt hast", "Für die Sinne"),
    ActivitySuggestion("Massiere deine Handflächen oder deine Schläfen sanft", "Für die Sinne"),
    ActivitySuggestion("Wickle dich in eine warme Decke und spüre die Geborgenheit", "Für die Sinne"),
    ActivitySuggestion("Nimm einen Schluck kaltes Wasser und spüre ihn bewusst", "Für die Sinne"),
    ActivitySuggestion("Lass kaltes oder warmes Wasser über deine Hände laufen", "Für die Sinne"),
    ActivitySuggestion("Konzentriere dich auf das Gefühl deiner Füße auf dem Boden", "Für die Sinne"),
    ActivitySuggestion("Höre einer Naturgeräusche-Playlist zu (Regen, Wald)", "Für die Sinne"),
    ActivitySuggestion("Beiß in eine Zitrone oder etwas anderes Saures", "Für die Sinne"),
    ActivitySuggestion("Rieche an einer Duftkerze oder einem ätherischen Öl", "Für die Sinne"),
    ActivitySuggestion("Betrachte ein Bild und beschreibe jedes Detail", "Für die Sinne"),
    ActivitySuggestion("Spüre das Gewicht deines Körpers auf dem Stuhl oder Bett", "Für die Sinne"),
    ActivitySuggestion("Iss ein Stück Schokolade und lass es langsam schmelzen", "Für die Sinne"),
    ActivitySuggestion("Achte auf alle Umgebungsgeräusche und identifiziere sie", "Für die Sinne"),
    ActivitySuggestion("Finde alle Schattierungen einer Farbe in deiner Umgebung", "Für die Sinne"),
    ActivitySuggestion("Presse deine Fingerspitzen sanft aneinander", "Für die Sinne"),
    ActivitySuggestion("Spüre die Temperatur der Luft auf deiner Haut", "Für die Sinne"),
    ActivitySuggestion("Kaue einen Minzkaugummi und konzentriere dich auf den Geschmack", "Für die Sinne"),
    ActivitySuggestion("Trage eine Creme auf und spüre die Berührung auf der Haut", "Für die Sinne"),
    ActivitySuggestion("Iss etwas Knackiges (Apfel, Karotte) und lausche dem Geräusch", "Für die Sinne"),

    // Für den Körper (körperliche Aktivierung)
    ActivitySuggestion("Mache 10 Kniebeugen oder Hampelmänner", "Für den Körper"),
    ActivitySuggestion("Dehne deinen Nacken und deine Schultern für 2 Minuten", "Für den Körper"),
    ActivitySuggestion("Tanze zu deinem Lieblingslied", "Für den Körper"),
    ActivitySuggestion("Gehe eine kleine Runde um den Block oder durch die Wohnung", "Für den Körper"),
    ActivitySuggestion("Räume einen kleinen Bereich in deinem Zimmer auf", "Für den Körper"),
    ActivitySuggestion("Drücke deine Hände für 10 Sekunden fest zusammen", "Für den Körper"),
    ActivitySuggestion("Jongliere mit zwei kleinen, weichen Gegenständen", "Für den Körper"),
    ActivitySuggestion("Spanne alle Muskeln an und lasse wieder los", "Für den Körper"),
    ActivitySuggestion("Laufe eine Minute auf der Stelle", "Für den Körper"),
    ActivitySuggestion("Strecke dich zur Decke, so hoch du kannst", "Für den Körper"),
    ActivitySuggestion("Mache 5 Liegestütze (an der Wand oder am Boden)", "Für den Körper"),
    ActivitySuggestion("Rolle deine Schultern 10 Mal vorwärts und rückwärts", "Für den Körper"),
    ActivitySuggestion("Balle deine Fäuste und öffne sie 15 Mal", "Für den Körper"),
    ActivitySuggestion("Gehe eine Treppe auf und ab", "Für den Körper"),
    ActivitySuggestion("Balanciere 30 Sekunden auf einem Bein, dann wechsle", "Für den Körper"),
    ActivitySuggestion("Schüttle deine Arme und Beine für eine Minute aus", "Für den Körper"),
    ActivitySuggestion("Mache einen 'Wandsitz' so lange du kannst", "Für den Körper"),
    ActivitySuggestion("Wackle mit deinen Zehen und Fingern", "Für den Körper"),
    ActivitySuggestion("Gib dir selbst eine feste Umarmung", "Für den Körper"),
    ActivitySuggestion("Boxe leicht in die Luft", "Für den Körper"),
    ActivitySuggestion("Mache ein paar Rumpfbeugen", "Für den Körper"),
    ActivitySuggestion("Tippe mit den Füßen rhythmisch auf den Boden", "Für den Körper"),
    ActivitySuggestion("Springe 10 Mal auf der Stelle", "Für den Körper"),
    ActivitySuggestion("Trage ein schweres Buch von einem Raum zum anderen", "Für den Körper"),
    ActivitySuggestion("Versuche, deine Zehen zu berühren", "Für den Körper")
)

private data class Category(val name: String, val icon: ImageVector)

private val categories = listOf(
    Category("Für den Kopf", Icons.Default.Psychology),
    Category("Für die Sinne", Icons.Default.SelfImprovement),
    Category("Für den Körper", Icons.Default.SportsEsports)
)

private enum class DistractionScreenState { INTRO, CATEGORY_CHOICE, SHAKE_SCREEN }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistractionSkillExercise(
    navController: NavController,
    from: String
) {
    var state by remember { mutableStateOf(DistractionScreenState.INTRO) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ablenkung als situativer Skill") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (state) {
                DistractionScreenState.INTRO -> DistractionIntroScreen { state = DistractionScreenState.CATEGORY_CHOICE }
                DistractionScreenState.CATEGORY_CHOICE -> CategoryChoiceScreen(
                    onCategorySelected = {
                        selectedCategory = it
                        state = DistractionScreenState.SHAKE_SCREEN
                    },
                    onFinish = { navController.navigate(Route.ExerciseRating.createRoute("distraction_skill_exercise", from)) }
                )
                DistractionScreenState.SHAKE_SCREEN -> {
                    val category = selectedCategory
                    if (category != null) {
                        ShakeScreen(
                            category = category,
                            onFinish = { navController.navigate(Route.ExerciseRating.createRoute("distraction_skill_exercise", from)) }
                        )
                    } else {
                        CategoryChoiceScreen(
                            onCategorySelected = { selected ->
                                selectedCategory = selected
                                state = DistractionScreenState.SHAKE_SCREEN
                            },
                            onFinish = { navController.navigate(Route.ExerciseRating.createRoute("distraction_skill_exercise", from)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DistractionIntroScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Finde deine Ablenkung", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(
            "Diese Übung hilft dir, in Momenten des Grübelns eine passende Ablenkung zu finden. Wähle eine Kategorie, die zu deiner aktuellen Stimmung passt.",
            textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("Starten") }
    }
}

@Composable
private fun CategoryChoiceScreen(onCategorySelected: (Category) -> Unit, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welche Art von Ablenkung brauchst du gerade?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        categories.forEach {
            CategoryCard(category = it, onClick = { onCategorySelected(it) })
            Spacer(Modifier.height(16.dp))
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onFinish) { Text("Übung beenden") }
    }
}

@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(category.icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Text(category.name, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun ShakeScreen(category: Category, onFinish: () -> Unit) {
    val context = LocalContext.current
    val suggestions = remember { activitySuggestions.filter { it.category == category.name } }
    var suggestion by remember { mutableStateOf(suggestions.random()) }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val shakeDetector = remember {
        object : SensorEventListener {
            private var lastUpdate: Long = 0
            private var last_x: Float = 0.0f
            private var last_y: Float = 0.0f
            private var last_z: Float = 0.0f
            private val SHAKE_THRESHOLD = 800

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent) {
                val curTime = System.currentTimeMillis()
                if ((curTime - lastUpdate) > 100) {
                    val diffTime = (curTime - lastUpdate)
                    lastUpdate = curTime

                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

                    if (speed > SHAKE_THRESHOLD) {
                        suggestion = suggestions.random()
                    }
                    last_x = x
                    last_y = y
                    last_z = z
                }
            }
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose { sensorManager.unregisterListener(shakeDetector) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Schüttle dein Handy für einen Vorschlag", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))

        AnimatedContent(targetState = suggestion.name, label = "") {
            Text(it, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { suggestion = suggestions.random() }) { Text("Neuer Vorschlag") }
            Button(onClick = onFinish) { Text("Das probiere ich!") }
        }
    }
}