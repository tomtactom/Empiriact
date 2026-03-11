package com.empiriact.app.ui.screens.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.launch

private data class IntroPage(
    val label: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val highlights: List<String>
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pages = remember {
        listOf(
            IntroPage(
                label = "Schritt 1 · Ankommen",
                icon = Icons.Default.AutoAwesome,
                title = "Willkommen bei Empiriact",
                subtitle = "Empiriact verbindet Empirie und ACT. Du nutzt wissenschaftlich fundierte Schritte für alltagsnahes, werteorientiertes Handeln.",
                highlights = listOf(
                    "Empirie macht Veränderungen sichtbar: du beobachtest, was wirkt.",
                    "ACT stärkt Selbstwirksamkeit: du richtest Handeln an deinen Werten aus.",
                    "Behavioral Activation bedeutet: kleine Handlungen bringen Aktivität und Orientierung in den Tag."
                )
            ),
            IntroPage(
                label = "Schritt 2 · Verstehen",
                icon = Icons.Default.PsychologyAlt,
                title = "Empirie im Alltag nutzen",
                subtitle = "Du sammelst eigene Erfahrung im Alltag und gewinnst Klarheit über hilfreiche Auslöser, Gedanken und Handlungen.",
                highlights = listOf(
                    "Kurze Check-ins zeigen Zusammenhänge zwischen Situation, Gefühl und Verhalten.",
                    "Deine Daten bleiben eine Grundlage für selbstbestimmte Entscheidungen.",
                    "So entsteht ein persönlicher Kompass für den nächsten realistischen Schritt."
                )
            ),
            IntroPage(
                label = "Schritt 3 · Dranbleiben",
                icon = Icons.Default.TrackChanges,
                title = "Behavioral Activation leben",
                subtitle = "Du setzt täglich kleine, konkrete Handlungen um. Damit wächst Schritt für Schritt aktive Teilhabe am eigenen Leben.",
                highlights = listOf(
                    "Werte geben Richtung für Entscheidungen im Alltag.",
                    "Regelmäßige Handlungen stärken Zuversicht und Handlungsfähigkeit.",
                    "Empiriact begleitet dich dabei, Verhalten bewusst und wirksam zu gestalten."
                )
            )
        )
    }

    val pageCount = pages.size + 2
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Einführung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TextButton(onClick = onFinished) { Text("Überspringen") }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when {
                page < pages.size -> IntroContentPage(page = pages[page])
                page == pages.size -> SystemPermissionsPage(onContinue = { scope.launch { pagerState.animateScrollToPage(page + 1) } })
                else -> OnboardingCompletionPage(onFinished = onFinished)
            }
        }

        if (pagerState.currentPage < pages.size) {
            Button(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("Weiter")
            }
        }

        PagerDots(pageCount = pageCount, currentPage = pagerState.currentPage, modifier = Modifier.padding(top = 12.dp, bottom = 16.dp))
    }
}

@Composable
private fun IntroContentPage(page: IntroPage) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = page.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(page.label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(page.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(page.subtitle, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                page.highlights.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}

@Composable
private fun SystemPermissionsPage(onContinue: () -> Unit) {
    val context = LocalContext.current
    val settingsRepository = remember(context) { SettingsRepository(context.applicationContext) }
    val permissionStateProvider = remember(context) { AndroidPermissionStateProvider(context.applicationContext) }
    val viewModel: OnboardingSetupViewModel = viewModel(
        factory = OnboardingSetupViewModel.Factory(settingsRepository, permissionStateProvider)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val notificationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        viewModel.onResume()
    }
    val activityLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (!granted) viewModel.onActivityRecognitionDenied()
        viewModel.onResume()
    }
    val batteryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.onResume()
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
        Text("Berechtigungen für stabile Begleitung", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Empiriact unterstützt dich im Alltag über Erinnerungen und optionale Sensorfunktionen. Du wählst jede Freigabe selbst und kannst sie jederzeit in Android anpassen.")
        Spacer(modifier = Modifier.height(16.dp))

        PermissionCard("Benachrichtigungen", "Erinnerungen helfen dir, geplante ACT-Schritte im Tagesverlauf umzusetzen.", "Du steuerst Benachrichtigungen jederzeit über Android-Einstellungen.", uiState.setupItems.notifications, "Benachrichtigungen aktivieren") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                openNotificationSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard("Akku-Optimierung", "Eine Freigabe verbessert die Zuverlässigkeit von Erinnerungen und Hintergrundabläufen.", "Die Einstellung bleibt unter deiner Kontrolle und ist rückgängig machbar.", uiState.setupItems.batteryOptimization, "Akku-Optimierung anpassen") {
            openBatteryOptimizationSettings(context) { batteryLauncher.launch(it) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard("Aktivitätserkennung", "Aktivitätserkennung ermöglicht optionale passive Marker wie Schrittzahlen.", "Die Funktion unterstützt empirische Selbstbeobachtung im Alltag.", uiState.setupItems.activityRecognition, if (uiState.setupItems.activityRecognition.required) "Aktivitätserkennung aktivieren" else "Bereits geklärt") {
            if (!uiState.setupItems.activityRecognition.required) return@PermissionCard
            activityLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        Spacer(modifier = Modifier.height(12.dp))

        DataCollectionCard(uiState.dataCollection, onDataDonationToggle = viewModel::setDataDonationEnabled, onPassiveMarkerToggle = viewModel::setPassiveMarkersOptIn)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Weiter zur Zusammenfassung") }
    }
}

@Composable
@Composable
private fun PermissionCard(
    title: String,
    benefit: String,
    privacyHint: String,
    state: SetupItemUiState,
    actionText: String,
    onAction: () -> Unit
) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(benefit)
            Spacer(modifier = Modifier.height(6.dp))
            Text(privacyHint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                if (state.enabled) "Status: Aktiv" else "Status: Offen",
                style = MaterialTheme.typography.labelLarge,
                color = if (state.enabled) MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onAction, modifier = Modifier.fillMaxWidth()) { Text(actionText) }
        }
    }
}

@Composable
private fun DataCollectionCard(
    state: DataCollectionUiState,
    onDataDonationToggle: (Boolean) -> Unit,
    onPassiveMarkerToggle: (Boolean) -> Unit
) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Datennutzung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Du entscheidest über anonymisierte Datenspende und optionale passive Marker. Beide Optionen unterstützen empirische Weiterentwicklung und deine Selbstbeobachtung.")
            SettingToggleRow("Anonymisierte Datenspende", state.dataDonationEnabled, onDataDonationToggle)
            SettingToggleRow("Passive Marker", state.passiveMarkersOptIn, onPassiveMarkerToggle)
            Text(
                text = if (state.done) "Status: Aktiv gewählt" else "Status: Bitte Auswahl treffen",
                style = MaterialTheme.typography.labelLarge,
                color = if (state.done) MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
        }
    }
}

@Composable
private fun SettingToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun OnboardingCompletionPage(onFinished: () -> Unit) {
    val context = LocalContext.current
    val settingsRepository = remember(context) { SettingsRepository(context.applicationContext) }
    val permissionStateProvider = remember(context) { AndroidPermissionStateProvider(context.applicationContext) }
    val viewModel: OnboardingSetupViewModel = viewModel(
        factory = OnboardingSetupViewModel.Factory(settingsRepository, permissionStateProvider)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
        Text("Du bist bereit", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Dein Einstieg ist abgeschlossen. Ab jetzt unterstützt dich Empiriact bei werteorientierter Behavioral Activation im Alltag.")
        Spacer(modifier = Modifier.height(16.dp))
        SummaryCard(uiState)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) { Text("App starten") }
    }
}

@Composable
private fun SummaryCard(uiState: OnboardingSetupUiState) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Onboarding-Zusammenfassung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("• Benachrichtigungen: ${if (uiState.setupItems.notifications.enabled) "Aktiv" else "Nicht aktiv"}")
            Text("• Akkuoptimierung: ${if (uiState.setupItems.batteryOptimization.enabled) "Deaktiviert" else "Aktiv"}")
            Text("• Aktivitätserkennung: ${if (uiState.setupItems.activityRecognition.enabled) "Aktiv" else "Nicht aktiv"}")
            Text("• Datenspende: ${if (uiState.dataCollection.dataDonationEnabled) "Aktiv" else "Nicht aktiv"}")
            Text("• Passive Marker: ${if (uiState.dataCollection.passiveMarkersOptIn) "Aktiv" else "Nicht aktiv"}")
        }
    }
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}

private fun openBatteryOptimizationSettings(context: Context, launchIntent: (Intent) -> Unit) {
    val requestIntent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:${context.packageName}"))
    val settingsIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    try {
        launchIntent(requestIntent)
    } catch (_: Exception) {
        launchIntent(settingsIntent)
    }
}

@Composable
private fun PagerDots(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Box(modifier = Modifier.padding(horizontal = 4.dp).size(if (index == currentPage) 10.dp else 8.dp).semantics {
                contentDescription = "Seite ${index + 1} von $pageCount"
            }.background(color = color, shape = CircleShape))
        }
    }
}
