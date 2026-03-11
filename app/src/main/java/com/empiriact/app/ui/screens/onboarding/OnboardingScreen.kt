package com.empiriact.app.ui.screens.onboarding

import android.Manifest
import android.content.Context
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.util.ActivityRecognitionPermissionUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Sicherer Raum · evidenzbasiert · in deinem Tempo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Einführung",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = onFinished) {
                Text("Überspringen")
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            if (page < pages.size) {
                IntroContentPage(page = pages[page])
            } else if (page == pages.size) {
                SystemPermissionsPage(
                    context = context,
                    onContinue = {
                        scope.launch { pagerState.animateScrollToPage(page + 1) }
                    }
                )
            } else {
                OnboardingCompletionPage(onFinished = onFinished)
            }
        }

        if (pagerState.currentPage < pages.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        }
                    },
                    enabled = pagerState.currentPage > 0
                ) {
                    Text("Zurück")
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < pageCount - 1) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            onFinished()
                        }
                    }
                ) {
                    Text("Weiter")
                }
            }
        }

        PagerDots(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
        )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = page.label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = page.subtitle,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                page.highlights.forEach { bullet ->
                    Text(text = "• $bullet", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Mini-Experiment für heute",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Freundlich mit dir selbst: Es geht nicht um Perfektion, sondern um einen hilfreichen nächsten Schritt.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = page.microAction, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SystemPermissionsPage(context: Context, onContinue: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val settingsRepository = remember(context) { SettingsRepository(context.applicationContext) }

    val dataDonationEnabled by settingsRepository.dataDonationEnabled.collectAsState(initial = false)

    var notificationsEnabled by remember { mutableStateOf(areNotificationsEnabled(context)) }
    var batteryOptimizationDisabled by remember { mutableStateOf(isIgnoringBatteryOptimizations(context)) }
    var activityRecognitionEnabled by remember {
        mutableStateOf(ActivityRecognitionPermissionUtils.hasPermission(context))
    }
    var notificationsDeferred by remember { mutableStateOf(false) }
    var batteryDeferred by remember { mutableStateOf(false) }
    var activityDeferred by remember { mutableStateOf(false) }
    var donationDeferred by remember { mutableStateOf(false) }

    val activityRecognitionRequired = ActivityRecognitionPermissionUtils.isPermissionRequired()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        notificationsEnabled = areNotificationsEnabled(context)
    }

    val activityRecognitionPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        activityRecognitionEnabled =
            granted || ActivityRecognitionPermissionUtils.hasPermission(context)
    }

    val batteryOptimizationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        batteryOptimizationDisabled = isIgnoringBatteryOptimizations(context)
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationsEnabled = areNotificationsEnabled(context)
                batteryOptimizationDisabled = isIgnoringBatteryOptimizations(context)
                activityRecognitionEnabled = ActivityRecognitionPermissionUtils.hasPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Setup in kleinen Schritten",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Wähle jetzt, was dich aktuell unterstützt. Jeder Punkt ist optional und später in den Einstellungen änderbar.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Setup-Checkliste",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        PermissionCard(
            title = "Benachrichtigungen",
            benefit = "Nutzen: Freundliche Erinnerungen helfen dir, kleine Schritte im Alltag leichter dranzubleiben.",
            privacyHint = "Datenschutz: Du bestimmst Zeitpunkt und Umfang. Inhalte aus Einträgen werden nicht für Werbung genutzt.",
            isEnabled = notificationsEnabled,
            deferred = notificationsDeferred,
            actionText = "Benachrichtigungen aktivieren",
            onDefer = { notificationsDeferred = true }
        ) {
            if (!notificationsEnabled) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                notificationsDeferred = false
            } else {
                openNotificationSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard(
            title = "Akku-Optimierung",
            benefit = "Nutzen: Erinnerungen und Hintergrundfunktionen laufen verlässlicher.",
            privacyHint = "Datenschutz: Diese Einstellung betrifft nur die Systemsteuerung im Hintergrund, nicht zusätzliche Datensammlung.",
            isEnabled = batteryOptimizationDisabled,
            deferred = batteryDeferred,
            actionText = "Akku-Optimierung anpassen",
            onDefer = { batteryDeferred = true }
        ) {
            batteryDeferred = false
            openBatteryOptimizationSettings(context, batteryOptimizationLauncher::launch)
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard(
            title = "Aktivitätserkennung",
            benefit = "Nutzen: Schritt-Trends können dir zusätzliche Rückmeldung über Aktivitätsmuster geben.",
            privacyHint = if (activityRecognitionRequired) {
                "Datenschutz: Verarbeitung erfolgt primär lokal. Die Berechtigung ist getrennt von der Datenspende und jederzeit widerrufbar."
            } else {
                "Datenschutz: Auf deinem Gerät ist keine zusätzliche Berechtigung nötig."
            },
            isEnabled = activityRecognitionEnabled,
            deferred = activityDeferred,
            actionText = if (activityRecognitionRequired) "Aktivitätserkennung aktivieren" else "Nicht erforderlich",
            onDefer = { activityDeferred = true }
        ) {
            if (!activityRecognitionRequired) {
                return@PermissionCard
            }

            if (!activityRecognitionEnabled) {
                activityRecognitionPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                activityDeferred = false
            } else {
                openAppDetailSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DataDonationCard(
            enabled = dataDonationEnabled,
            onOptIn = {
                donationDeferred = false
                scope.launch { settingsRepository.setDataDonationEnabled(true) }
            },
            onOptOut = {
                donationDeferred = false
                scope.launch { settingsRepository.setDataDonationEnabled(false) }
            },
            onDefer = {
                donationDeferred = true
                scope.launch { settingsRepository.setDataDonationEnabled(false) }
            },
            deferred = donationDeferred
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
            Text("Weiter zur Zusammenfassung")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationsEnabled) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Hinweis: Ab Android 13 braucht die App eine aktive Benachrichtigungsfreigabe.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DataDonationCard(
    enabled: Boolean,
    onOptIn: () -> Unit,
    onOptOut: () -> Unit,
    onDefer: () -> Unit,
    deferred: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Datenspende: bewusst entscheiden",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Nutzen: Mit optionalen anonymisierten Nutzungsdaten kann Empiriact wissenschaftlich weiter verbessert werden.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Datenschutz: Die Datenspende ist freiwillig und getrennt von der Kernnutzung. Keine freien Texte werden für Werbung verkauft.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (enabled) "Status: Aktiviert" else if (deferred) "Status: Nicht aktiviert (vorerst übersprungen)" else "Status: Nicht aktiviert",
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) MaterialTheme.colorScheme.primary else Color(0xFFB45309)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onOptOut, modifier = Modifier.weight(1f)) {
                    Text("Nein, nicht teilen")
                }
                Button(onClick = onOptIn, modifier = Modifier.weight(1f)) {
                    Text("Ja, anonym teilen")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onDefer, modifier = Modifier.align(Alignment.End)) {
                Text("Später")
            }
        }
    }
}

@Composable
private fun SummaryCard(
    notificationsEnabled: Boolean,
    batteryOptimizationDisabled: Boolean,
    activityRecognitionEnabled: Boolean,
    dataDonationEnabled: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Onboarding-Zusammenfassung",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = "• Benachrichtigungen: ${if (notificationsEnabled) "Aktiv" else "Nicht aktiv"}")
            Text(text = "• Akkuoptimierung: ${if (batteryOptimizationDisabled) "Deaktiviert" else "Aktiv"}")
            Text(text = "• Aktivitätserkennung: ${if (activityRecognitionEnabled) "Aktiv" else "Nicht aktiv"}")
            Text(text = "• Datenspende: ${if (dataDonationEnabled) "Aktiv" else "Nicht aktiv"}")
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    benefit: String,
    privacyHint: String,
    isEnabled: Boolean,
    deferred: Boolean,
    actionText: String,
    onDefer: () -> Unit,
    onAction: () -> Unit
) {
    val statusColor = if (isEnabled) {
        MaterialTheme.colorScheme.primary
    } else {
        Color(0xFFB45309)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = benefit, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = privacyHint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isEnabled) "Status: Aktiviert" else if (deferred) "Status: Nicht aktiviert (vorerst übersprungen)" else "Status: Nicht aktiviert",
                style = MaterialTheme.typography.labelLarge,
                color = statusColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onAction, modifier = Modifier.weight(1f)) {
                    Text(actionText)
                }
                TextButton(onClick = onDefer, modifier = Modifier.weight(1f)) {
                    Text("Später")
                }
            }
        }
    }
}

@Composable
private fun OnboardingCompletionPage(onFinished: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Abschluss",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Du bist startklar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Danke, dass du dir Zeit für dein Setup genommen hast. Kleine, regelmäßige Schritte reichen vollkommen aus.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Zusammenfassung:\n• Du hast die wichtigsten Systempunkte geprüft.\n• Nicht aktivierte Punkte sind klar markiert und blockieren dich nicht.\n• Du kannst jederzeit mit einem nächsten kleinen Schritt weitermachen.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) {
            Text("App starten")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Hinweis: Alles ist jederzeit in den Einstellungen änderbar.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun areNotificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}

private fun openAppDetailSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}

private fun openBatteryOptimizationSettings(
    context: Context,
    launchIntent: (Intent) -> Unit
) {
    val requestIntent = Intent(
        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        Uri.parse("package:${context.packageName}")
    )

    val settingsIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)

    try {
        launchIntent(requestIntent)
    } catch (_: Exception) {
        launchIntent(settingsIntent)
    }
}

@Composable
private fun PagerDots(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
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
