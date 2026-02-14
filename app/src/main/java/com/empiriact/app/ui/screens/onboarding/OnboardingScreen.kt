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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
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
                label = "Schritt 1 · Orientierung",
                icon = Icons.Default.AutoAwesome,
                title = "Willkommen bei Empiriact",
                subtitle = "Du lernst, was gerade in dir passiert, ohne dich davon steuern zu lassen. Das ist die Grundlage für ruhige, wirksame Entscheidungen im Alltag.",
                highlights = listOf(
                    "Kurze Check-ins statt Überforderung: 30–90 Sekunden reichen für einen guten Start.",
                    "Verhaltenstherapeutisch fundiert: Wahrnehmen → benennen → handlungsfähig bleiben.",
                    "Keine Perfektion nötig: kleine Schritte sind wirksamer als seltene große Vorsätze."
                ),
                microAction = "Nimm dir heute einmal 1 Minute für einen bewussten Atemzug + Körper-Check."
            ),
            IntroPage(
                label = "Schritt 2 · In schwierigen Momenten",
                icon = Icons.Default.PsychologyAlt,
                title = "Wenn der Kopf laut wird",
                subtitle = "Empiriact unterstützt dich in Stressmomenten mit klaren, konkreten Übungen, damit aus Grübeln wieder Orientierung wird.",
                highlights = listOf(
                    "Defusion: Gedanken als Gedanken erkennen – nicht als Befehl.",
                    "Emotionsregulation: Anspannung spürbar senken, bevor du entscheidest.",
                    "Soforthilfe mit Struktur: Beobachten, erden, nächsten hilfreichen Schritt wählen."
                ),
                microAction = "Lege innerlich den Satz fest: ‚Ich bemerke gerade den Gedanken, dass …‘"
            ),
            IntroPage(
                label = "Schritt 3 · Wertebasiert handeln",
                icon = Icons.Default.TrackChanges,
                title = "Vom Verstehen ins Tun",
                subtitle = "Du verknüpfst Übungen mit deinen Werten, damit Veränderungen im Alltag stabil und realistisch werden.",
                highlights = listOf(
                    "Kleine Commitments: 1 konkreter Schritt pro Tag schlägt vage Vorsätze.",
                    "Selbstwirksamkeit wächst durch Wiederholung, nicht durch Druck.",
                    "Du entscheidest das Tempo – die App passt sich deinem Alltag an."
                ),
                microAction = "Wähle heute einen Mini-Schritt, der zu einem deiner Werte passt."
            )
        )
    }

    val pageCount = pages.size + 1
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            } else {
                SystemPermissionsPage(context = context, onFinished = onFinished)
            }
        }

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
                    }
                }
            ) {
                Text(if (pagerState.currentPage == pageCount - 1) "Fertig" else "Weiter")
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Mini-Experiment für heute",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = page.microAction, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SystemPermissionsPage(context: Context, onFinished: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var notificationsEnabled by remember { mutableStateOf(areNotificationsEnabled(context)) }
    var batteryOptimizationDisabled by remember { mutableStateOf(isIgnoringBatteryOptimizations(context)) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        notificationsEnabled = areNotificationsEnabled(context)
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
            text = "Letzter Schritt: App zuverlässig machen",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Du entscheidest selbst. Die folgenden Optionen erhöhen die Zuverlässigkeit von Erinnerungen und Übungen deutlich.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionCard(
            title = "Benachrichtigungen erlauben",
            description = "So können wir dir achtsame, kurze Impulse zur passenden Zeit senden.",
            isEnabled = notificationsEnabled,
            enabledText = "Aktiv",
            disabledText = "Nicht aktiv",
            actionText = "Jetzt erlauben"
        ) {
            if (!notificationsEnabled) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                openNotificationSettings(context)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PermissionCard(
            title = "Akkuoptimierung deaktivieren",
            description = "Damit Empiriact im Hintergrund stabil läuft und Erinnerungen nicht ausfallen.",
            isEnabled = batteryOptimizationDisabled,
            enabledText = "Deaktiviert",
            disabledText = "Aktiv",
            actionText = "Einstellung öffnen"
        ) {
            openBatteryOptimizationSettings(context, batteryOptimizationLauncher::launch)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinished,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Jetzt mit Empiriact starten")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationsEnabled) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Hinweis: Ab Android 13 sind Benachrichtigungen standardmäßig deaktiviert.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    isEnabled: Boolean,
    enabledText: String,
    disabledText: String,
    actionText: String,
    onAction: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isEnabled) "Status: $enabledText" else "Status: $disabledText",
                style = MaterialTheme.typography.labelLarge,
                color = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onAction,
                modifier = Modifier.widthIn(min = 180.dp)
            ) {
                Text(actionText)
            }
        }
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
