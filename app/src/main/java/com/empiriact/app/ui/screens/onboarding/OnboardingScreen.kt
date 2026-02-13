package com.empiriact.app.ui.screens.onboarding

import android.Manifest
import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch

private data class IntroPage(
    val title: String,
    val subtitle: String,
    val bullets: List<String>
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val pages = remember {
        listOf(
            IntroPage(
                title = "Willkommen bei Empiriact",
                subtitle = "Schön, dass du da bist. Du bestimmst dein Tempo – wir begleiten dich Schritt für Schritt.",
                bullets = listOf(
                    "Du musst nichts perfekt machen: kleine Schritte zählen.",
                    "Wir arbeiten mit alltagsnahen, verhaltenstherapeutischen Übungen.",
                    "Du kannst jederzeit überspringen, pausieren oder später anpassen."
                )
            ),
            IntroPage(
                title = "So unterstützt dich die App",
                subtitle = "Damit du sofort Orientierung hast, hier der Überblick über die wichtigsten Bereiche.",
                bullets = listOf(
                    "Heute: kurze, machbare Impulse für den aktuellen Tag.",
                    "Check-in & Übersicht: Muster erkennen und Fortschritte sehen.",
                    "Lernen & Ressourcen: Wissen + konkrete Übungen für den Alltag."
                )
            ),
            IntroPage(
                title = "Empathisch, klar, ohne Druck",
                subtitle = "Moderne Verhaltensänderung funktioniert besser mit Selbstbestimmung statt Zwang.",
                bullets = listOf(
                    "Du entscheidest, welche Übungen zu dir passen.",
                    "Reflexion statt Selbstkritik: neugierig beobachten, nicht bewerten.",
                    "Konsistenz ist wichtiger als Intensität – auch 2 Minuten sind wertvoll."
                )
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
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = onFinished) {
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
                SystemPermissionsPage(context = context)
            }
        }

        PagerDots(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (pagerState.currentPage == 0) {
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (pagerState.currentPage == 0) "Später" else "Zurück")
            }

            Button(
                onClick = {
                    if (pagerState.currentPage == pageCount - 1) {
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (pagerState.currentPage == pageCount - 1) "Los geht's" else "Weiter")
            }
        }
    }
}

@Composable
private fun IntroContentPage(page: IntroPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
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
                page.bullets.forEach { bullet ->
                    Text(text = "• $bullet", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SystemPermissionsPage(context: Context) {
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
            text = "Noch 2 wichtige Einstellungen",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Du entscheidest selbst. Diese Freigaben helfen der App nur dabei, dich zuverlässig zu unterstützen – auch im Hintergrund.",
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
                    .size(8.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}
