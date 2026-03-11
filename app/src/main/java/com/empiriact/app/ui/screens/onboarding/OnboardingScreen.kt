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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.Shield
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
    val highlights: List<String>,
    val microAction: String
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
                subtitle = "Du kannst hier in deinem Tempo erkunden, was dir im Alltag guttut. Jeder Check-in unterstützt dich dabei, klarer wahrzunehmen und stimmige Entscheidungen zu treffen.",
                highlights = listOf(
                    "Mikro-Check-ins (30–90 Sekunden) lassen sich leicht in deinen Tag integrieren.",
                    "Verhaltenstherapeutische Kernkompetenz: Wahrnehmen → benennen → bewusst handeln.",
                    "Du wählst, was gerade passend ist: ein kleiner Schritt zählt voll."
                ),
                microAction = "Wenn du magst: Nimm dir heute 1 Minute für Atem und Körperwahrnehmung."
            ),
            IntroPage(
                label = "Schritt 2 · Flexibel bleiben",
                icon = Icons.Default.PsychologyAlt,
                title = "Wenn Gedanken intensiv werden",
                subtitle = "Empiriact bietet dir kurze, strukturierte Übungen, mit denen du Abstand gewinnst und wieder handlungsfähig wirst.",
                highlights = listOf(
                    "Kognitive Defusion: Gedanken als mentale Ereignisse einordnen.",
                    "Emotionsregulation: Erregung gezielt herunterfahren und Orientierung stärken.",
                    "Situative Selbststeuerung: Beobachten, erden, nächsten hilfreichen Schritt wählen."
                ),
                microAction = "Wenn es für dich passt: Nutze den Satz „Ich bemerke den Gedanken, dass …“ als Anker."
            ),
            IntroPage(
                label = "Schritt 3 · Werteorientiert handeln",
                icon = Icons.Default.TrackChanges,
                title = "Vom Verstehen ins Tun",
                subtitle = "Du verbindest Übungen mit deinen persönlichen Werten, damit hilfreiche Gewohnheiten nachhaltig in deinen Alltag wachsen.",
                highlights = listOf(
                    "Werteklärung: Du definierst, was dir wichtig ist und wie es im Alltag sichtbar wird.",
                    "Konkrete Verhaltensplanung: Ein umsetzbarer Schritt pro Tag schafft Kontinuität.",
                    "Selbstwirksamkeit entsteht durch wiederholte Erfahrungen in deinem eigenen Tempo."
                ),
                microAction = "Wenn du möchtest: Wähle heute einen Mini-Schritt, der zu einem deiner Werte passt."
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Sicherer Raum · evidenzbasiert · in deinem Tempo", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { if (pagerState.currentPage > 0) scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                    enabled = pagerState.currentPage > 0
                ) { Text("Zurück") }

                Button(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }) { Text("Weiter") }
            }
        }

        PagerDots(pageCount = pageCount, currentPage = pagerState.currentPage, modifier = Modifier.padding(top = 12.dp, bottom = 16.dp))
    }
}

@Composable
private fun IntroContentPage(page: IntroPage) { /* unchanged content */
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
        Spacer(modifier = Modifier.height(16.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Mini-Experiment für heute", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                }
                Text("Freundlich mit dir selbst: Es geht nicht um Perfektion, sondern um einen hilfreichen nächsten Schritt.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(page.microAction, style = MaterialTheme.typography.bodyMedium)
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
        Text("Setup in kleinen Schritten", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Du entscheidest bewusst, was du jetzt aktivierst. Alles ist später in den Einstellungen anpassbar.")
        Spacer(modifier = Modifier.height(16.dp))

        SetupChecklistOverview(uiState)
        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard("Benachrichtigungen", "Erinnerungen unterstützen dich dabei, Check-ins im Alltag nicht zu übersehen.", "Steuerung jederzeit über Android-Einstellungen möglich.", uiState.setupItems.notifications, "Benachrichtigungen aktivieren", onDefer = { viewModel.defer(SetupItemType.NOTIFICATIONS) }) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                openNotificationSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard("Akku-Optimierung", "Ohne Einschränkung kann Empiriact Erinnerungen zuverlässiger ausführen.", "Du kannst die Ausnahme jederzeit zurücknehmen.", uiState.setupItems.batteryOptimization, "Akku-Optimierung anpassen", onDefer = { viewModel.defer(SetupItemType.BATTERY_OPTIMIZATION) }) {
            openBatteryOptimizationSettings(context) { batteryLauncher.launch(it) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard("Aktivitätserkennung", "Für passive Schrittmarker benötigt Android ggf. diese Berechtigung.", "Nur relevant, wenn passive Marker genutzt werden.", uiState.setupItems.activityRecognition, if (uiState.setupItems.activityRecognition.required) "Aktivitätserkennung aktivieren" else "Nicht erforderlich", onDefer = { viewModel.defer(SetupItemType.ACTIVITY_RECOGNITION) }) {
            if (!uiState.setupItems.activityRecognition.required) return@PermissionCard
            activityLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        Spacer(modifier = Modifier.height(12.dp))

        DataCollectionCard(uiState.dataCollection, onDefer = { viewModel.defer(SetupItemType.DATA_COLLECTION) }, onDataDonationToggle = viewModel::setDataDonationEnabled, onPassiveMarkerToggle = viewModel::setPassiveMarkersOptIn)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Weiter zur Zusammenfassung") }
    }
}

@Composable
private fun SetupChecklistOverview(uiState: OnboardingSetupUiState) {
    val items = listOf(
        "Benachrichtigungen" to uiState.setupItems.notifications.enabled,
        "Akku-Optimierung" to uiState.setupItems.batteryOptimization.enabled,
        "Aktivitätserkennung" to uiState.setupItems.activityRecognition.enabled,
        "Datennutzung" to uiState.dataCollection.done
    )
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Setup-Checkliste", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            items.forEach { (label, done) -> Text("${if (done) "☑" else "☐"} $label", color = if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) }
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    benefit: String,
    privacyHint: String,
    state: SetupItemUiState,
    actionText: String,
    onDefer: () -> Unit,
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
                if (state.enabled) "Status: Aktiviert" else if (state.deferred) "Status: Nicht aktiviert (vorerst übersprungen)" else "Status: Nicht aktiviert",
                style = MaterialTheme.typography.labelLarge,
                color = if (state.enabled) MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onAction, modifier = Modifier.weight(1f)) { Text(actionText) }
                TextButton(onClick = onDefer, modifier = Modifier.weight(1f)) { Text("Später") }
            }
        }
    }
}

@Composable
private fun DataCollectionCard(
    state: DataCollectionUiState,
    onDefer: () -> Unit,
    onDataDonationToggle: (Boolean) -> Unit,
    onPassiveMarkerToggle: (Boolean) -> Unit
) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Datenspende & passive Marker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Du kannst wissenschaftliche Verbesserung (Datenspende) und passive Marker separat steuern.")
            SettingToggleRow("Anonymisierte Datenspende", state.dataDonationEnabled, onDataDonationToggle)
            SettingToggleRow("Passive Marker", state.passiveMarkersOptIn, onPassiveMarkerToggle)
            Text(
                text = if (state.done) "Status: Entscheidung getroffen" else "Status: Noch offen",
                style = MaterialTheme.typography.labelLarge,
                color = if (state.done) MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
            TextButton(onClick = onDefer, modifier = Modifier.align(Alignment.End)) { Text("Später") }
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
        Text("Abschluss", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Du bist startklar", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Danke, dass du dir Zeit für dein Setup genommen hast. Kleine, regelmäßige Schritte reichen vollkommen aus.")
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
