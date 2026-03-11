package com.empiriact.app.ui.screens.onboarding

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.content.ContextCompat
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
    val scope = rememberCoroutineScope()
    val pages = remember {
        listOf(
            IntroPage(
                label = "Schritt 1 · Orientierung",
                icon = Icons.Default.AutoAwesome,
                title = "Willkommen bei Empiriact",
                subtitle = "Der Name Empiriact verbindet Empirie und ACT: du sammelst alltagsnahe Beobachtungen und setzt werteorientiertes Handeln bewusst um.",
                highlights = listOf(
                    "Empirie bedeutet hier: du prüfst mit deinen eigenen Daten, was dir im Alltag wirklich hilft.",
                    "ACT unterstützt Selbstwirksamkeit: du triffst Entscheidungen auf Basis deiner Werte.",
                    "Behavioral Activation heißt: kleine machbare Handlungen bringen dich zuverlässig in Bewegung."
                )
            ),
            IntroPage(
                label = "Schritt 2 · Anwendung",
                icon = Icons.Default.PsychologyAlt,
                title = "Empirie im Alltag nutzen",
                subtitle = "Regelmäßige Einträge machen Muster sichtbar. Du erkennst förderliche Situationen und leitest konkrete nächste Schritte für deinen Tag ab.",
                highlights = listOf(
                    "Du beobachtest Verhalten, Stimmung und Kontext strukturiert.",
                    "Du entwickelst realistische Schritte, die du direkt umsetzen kannst.",
                    "Du trainierst flexible Aufmerksamkeit und aktives Handeln im Alltag."
                )
            ),
            IntroPage(
                label = "Schritt 3 · Aktiv werden",
                icon = Icons.Default.TrackChanges,
                title = "Behavioral Activation klar umsetzen",
                subtitle = "Du setzt tägliche Handlungen mit kleinem Einstieg um und stärkst dadurch Handlungsfähigkeit, Stabilität und Selbstvertrauen.",
                highlights = listOf(
                    "Du wählst Handlungen, die zu deinen Werten und deinem aktuellen Energielevel passen.",
                    "Du wiederholst hilfreiche Schritte und baust tragfähige Routinen auf.",
                    "Du übernimmst aktiv Verantwortung für deinen Weg im Alltag."
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Einführung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when {
                page < pages.size -> IntroContentPage(page = pages[page])
                page == pages.size -> SystemPermissionsPage(onContinue = { scope.launch { pagerState.animateScrollToPage(page + 1) } })
                else -> OnboardingCompletionPage(onFinished = onFinished)
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < pageCount - 1) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    onFinished()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(if (pagerState.currentPage == pageCount - 1) "App starten" else "Weiter")
        }

        PagerDots(pageCount = pageCount, currentPage = pagerState.currentPage, modifier = Modifier.padding(top = 12.dp, bottom = 16.dp))
    }
}

@Composable
private fun IntroContentPage(page: IntroPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
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
    var showNotificationSettingsHint by rememberSaveable { mutableStateOf(false) }
    var hasRequestedNotificationPermission by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
        Text("Berechtigungen und Datennutzung", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Du richtest Empiriact hier einheitlich ein. Jede Freigabe unterstützt alltagsnahe Begleitung und bleibt jederzeit durch dich anpassbar.")
        Spacer(modifier = Modifier.height(16.dp))

        PermissionCard(
            title = "Benachrichtigungen",
            description = "Erinnerungen unterstützen dich bei geplanten Schritten der Behavioral Activation.",
            statusText = if (uiState.setupItems.notifications.enabled) "Aktiv" else "Offen",
            actionText = "Freigabe öffnen",
            helperText = if (showNotificationSettingsHint) "Wenn der Dialog nicht erscheint, öffne bitte die Einstellungen." else null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when {
                    isPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS) -> {
                        showNotificationSettingsHint = false
                        viewModel.onResume()
                    }

                    isNotificationPermissionPermanentlyDenied(
                        context = context,
                        hasRequestedPermission = hasRequestedNotificationPermission
                    ) -> {
                        showNotificationSettingsHint = true
                        openNotificationSettings(context)
                    }

                    else -> {
                        showNotificationSettingsHint = false
                        hasRequestedNotificationPermission = true
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            } else {
                showNotificationSettingsHint = false
                openNotificationSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        PermissionCard(
            title = "Akku-Optimierung",
            description = "Eine passende Einstellung verbessert die Zuverlässigkeit von Erinnerungen im Hintergrund.",
            statusText = if (uiState.setupItems.batteryOptimization.enabled) "Aktiv" else "Offen",
            actionText = "Einstellungen öffnen"
        ) {
            openBatteryOptimizationSettings(context) { batteryLauncher.launch(it) }
        }

        Spacer(modifier = Modifier.height(10.dp))

        PermissionCard(
            title = "Aktivitätserkennung",
            description = "Optional für passive Marker wie Schritte, damit Alltagstrends besser sichtbar werden.",
            statusText = if (uiState.setupItems.activityRecognition.enabled || !uiState.setupItems.activityRecognition.required) "Aktiv" else "Offen",
            actionText = if (uiState.setupItems.activityRecognition.required) "Freigabe öffnen" else "Bereits geklärt"
        ) {
            if (!uiState.setupItems.activityRecognition.required) return@PermissionCard
            activityLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        Spacer(modifier = Modifier.height(10.dp))

        DataCollectionCard(
            state = uiState.dataCollection,
            onDataDonationToggle = viewModel::setDataDonationEnabled,
            onPassiveMarkerToggle = viewModel::setPassiveMarkersOptIn
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Weiter zur Zusammenfassung") }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    statusText: String,
    actionText: String,
    helperText: String? = null,
    onAction: () -> Unit
) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Status: $statusText",
                style = MaterialTheme.typography.labelLarge,
                color = if (statusText == "Aktiv") MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
            helperText?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = onAction, modifier = Modifier.fillMaxWidth()) { Text(actionText) }
        }
    }
}

private fun isPermissionGranted(context: Context, permission: String): Boolean =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

private fun isNotificationPermissionPermanentlyDenied(context: Context, hasRequestedPermission: Boolean): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false
    if (!hasRequestedPermission) return false
    if (isPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS)) return false

    val activity = context.findActivity() ?: return false
    return !activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
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
            Text("Du steuerst Datenspende und passive Marker selbst. Beide Optionen dienen der empirischen Auswertung und deiner individuellen Orientierung im Alltag.")
            SettingToggleRow("Anonymisierte Datenspende", state.dataDonationEnabled, onDataDonationToggle)
            SettingToggleRow("Passive Marker", state.passiveMarkersOptIn, onPassiveMarkerToggle)
            Text(
                text = if (state.done) "Status: Auswahl gesetzt" else "Status: Auswahl ausstehend",
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
        Text("Dein Start ist vorbereitet", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Empiriact begleitet dich ab jetzt mit empirischer Selbstbeobachtung und werteorientierter Behavioral Activation im Alltag.")
        Spacer(modifier = Modifier.height(16.dp))
        SummaryCard(uiState)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = onFinished, modifier = Modifier.fillMaxWidth()) { Text("App starten") }
    }
}

@Composable
private fun SummaryCard(uiState: OnboardingSetupUiState) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Zusammenfassung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
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
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (index == currentPage) 10.dp else 8.dp)
                    .semantics {
                        contentDescription = "Seite ${index + 1} von $pageCount"
                    }
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}
