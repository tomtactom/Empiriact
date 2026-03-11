package com.empiriact.app.ui.screens.today

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.ui.theme.Dimensions
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private sealed class TodayScreenItem {
    data class Header(val date: LocalDate) : TodayScreenItem()
    data class Hour(val hour: Int, val date: LocalDate) : TodayScreenItem()
    object ShowMoreButton : TodayScreenItem()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as EmpiriactApplication
    val vm: TodayViewModel = viewModel(factory = application.viewModelFactory)
    val logs by vm.todayLogs.collectAsState()
    val uniqueActivities by vm.uniqueActivities.collectAsState()
    val uniquePeople by vm.uniquePeople.collectAsState()
    val unsavedChanges by vm.unsavedChanges.collectAsState()
    val todayIntroCompleted by vm.todayIntroCompleted.collectAsState()
    val baselineUiStatus by vm.baselineUiStatus.collectAsState()
    val baCriteriaAcknowledged by vm.baActivityCriteriaAcknowledged.collectAsState()
    val baPreferenceTags by vm.baActivityPreferenceTags.collectAsState()
    val baMaintenanceUiStatus by vm.baMaintenanceUiStatus.collectAsState()

    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val currentHour = now.hour
    var expandedEntry by remember { mutableStateOf<HourEntryKey?>(null) }
    var showAllHours by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }
    var showBaselineInfoCard by rememberSaveable { mutableStateOf(true) }

    // Initialize expanded entry only once on first load
    LaunchedEffect(isInitialized) {
        if (!isInitialized && logs.findForHourOnDate(currentHour, today) == null) {
            expandedEntry = HourEntryKey(today, currentHour)
            isInitialized = true
        } else if (!isInitialized) {
            isInitialized = true
        }
    }

    val items = remember(showAllHours, currentHour, today, logs) {
        val hoursInOrder = (currentHour downTo 0) + (23 downTo currentHour + 1)
        val displayedHours = if (showAllHours) hoursInOrder else hoursInOrder.take(3)

        val listItems = mutableListOf<TodayScreenItem>()
        displayedHours.groupBy {
            if (it > currentHour) now.toLocalDate().minusDays(1) else now.toLocalDate()
        }.forEach { (date, hours) ->
            listItems.add(TodayScreenItem.Header(date))
            hours.forEach { hour ->
                listItems.add(TodayScreenItem.Hour(hour, date))
            }
        }

        if (!showAllHours && hoursInOrder.size > 3) {
            listItems.add(TodayScreenItem.ShowMoreButton)
        }
        listItems
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
        contentPadding = PaddingValues(vertical = Dimensions.paddingMedium)
    ) {
        if (!todayIntroCompleted) {
            item(key = "today_intro") {
                TodayIntroCoachCard(
                    criteriaAcknowledged = baCriteriaAcknowledged,
                    selectedPreferenceTags = baPreferenceTags,
                    onTogglePreferenceTag = vm::toggleBaActivityPreferenceTag,
                    onAcknowledgeCriteria = vm::acknowledgeActivityCriteria,
                    onStart = {
                        expandedEntry = HourEntryKey(today, currentHour)
                    },
                    onDismiss = { vm.completeTodayIntro() }
                )
            }
        }

        if (baselineUiStatus.isBaselineMode && showBaselineInfoCard) {
            item(key = "baseline_info") {
                BaselineInfoCard(
                    hint = baselineUiStatus.observationHint.orEmpty(),
                    onDismiss = { showBaselineInfoCard = false }
                )
            }
        }

        if (baMaintenanceUiStatus.isActive) {
            item(key = "maintenance_notice") {
                MaintenanceModeNoticeCard(
                    onSwitchToDaily = vm::switchMaintenanceToDaily
                )
            }
        }

        items(
            items = items,
            key = { item ->
                when (item) {
                    is TodayScreenItem.Header -> "header_${item.date.toEpochDay()}"
                    is TodayScreenItem.Hour -> "hour_${item.date.toEpochDay()}_${item.hour}"
                    is TodayScreenItem.ShowMoreButton -> "show_more"
                }
            }
        ) { item ->
            when (item) {
                is TodayScreenItem.Header -> DateHeader(date = item.date)
                is TodayScreenItem.Hour -> {
                    HourEntry(
                        hour = item.hour,
                        log = logs.findForHourOnDate(item.hour, item.date),
                        isExpanded = expandedEntry == HourEntryKey(item.date, item.hour),
                        onExpand = {
                            expandedEntry = if (expandedEntry == HourEntryKey(item.date, item.hour)) {
                                null
                            } else {
                                HourEntryKey(item.date, item.hour)
                            }
                        },
                        onSave = { activity, valence, people, durationMinutes, difficultyRating, activationLatencyMinutes ->
                            vm.upsertActivityForHour(
                                item.date,
                                item.hour,
                                activity,
                                valence,
                                people,
                                durationMinutes,
                                difficultyRating,
                                activationLatencyMinutes
                            )
                        },
                        suggestions = uniqueActivities,
                        peopleSuggestions = uniquePeople,
                        cachedData = unsavedChanges[HourEntryKey(item.date, item.hour)],
                        onCacheChanged = { cache -> vm.cacheHourEntry(item.date, item.hour, cache) },
                        selectedPreferenceTags = baPreferenceTags
                    )
                }
                is TodayScreenItem.ShowMoreButton -> {
                    Button(
                        onClick = { showAllHours = true },
                        modifier = Modifier.padding(top = Dimensions.paddingMedium)
                    ) {
                        Text("Mehr anzeigen")
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayIntroCoachCard(
    criteriaAcknowledged: Boolean,
    selectedPreferenceTags: Set<String>,
    onTogglePreferenceTag: (String) -> Unit,
    onAcknowledgeCriteria: () -> Unit,
    onStart: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
            modifier = Modifier.padding(Dimensions.paddingMedium)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Beobachtungsplan: Dein Einstieg",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            Text(
                text = "Keine Bewertung – nur Beobachtung. Der Beobachtungsplan zeigt, welche Aktivitäten sich tatsächlich ereignen. Das ist der erste Schritt der Verhaltensaktivierung.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tipp: Jede Stunde zählt – auch Routine und \"unproduktive\" Zeiten sind wertvoll für das Verständnis.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Was zählt hier als Aktivität?",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Alles, was du bewusst tust: Eigeninitiative, kleine Schritte oder Routine. Es geht um Beobachtung statt Bewertung – bitte freundlich mit dir sprechen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            TextButton(onClick = onAcknowledgeCriteria) {
                Text(if (criteriaAcknowledged) "✓ Leitlinien gespeichert" else "Leitlinien verstanden")
            }
            val introOptionalTags = listOf("Kleiner Schritt", "Eigeninitiative", "Routine", "Aktive Handlung")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                introOptionalTags.forEach { tag ->
                    FilterChip(
                        selected = selectedPreferenceTags.contains(tag),
                        onClick = { onTogglePreferenceTag(tag) },
                        label = { Text(tag) }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Später")
                }
                Button(onClick = {
                    onStart()
                    onDismiss()
                }, modifier = Modifier.weight(1f)) {
                    Text("Jetzt starten")
                }
            }
        }
    }
}



@Composable
private fun MaintenanceModeNoticeCard(
    onSwitchToDaily: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
            modifier = Modifier.padding(Dimensions.paddingMedium)
        ) {
            Text(
                text = "Heute kein Pflicht-Check-in – du kannst trotzdem eintragen",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = "Wenn es dir hilft, kannst du mit einem Tipp wieder auf tägliche Erinnerung umstellen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            TextButton(onClick = onSwitchToDaily) {
                Text("Täglich erinnern")
            }
        }
    }
}

@Composable
private fun BaselineInfoCard(
    hint: String,
    onDismiss: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
            modifier = Modifier.padding(Dimensions.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Baseline-Zielsetzung (optional)",
                    style = MaterialTheme.typography.titleMedium
                )
                FilledTonalButton(onClick = onDismiss) {
                    Text("Ausblenden")
                }
            }
            if (hint.isNotBlank()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = "Fokus: Beobachte heute möglichst neutral, was du tust – ohne Selbstkritik oder Leistungsdruck. Jede Eintragung hilft, Muster sichtbar zu machen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun DateHeader(date: LocalDate) {
    val now = LocalDate.now()
    val daysBetween = ChronoUnit.DAYS.between(date, now)

    val text = when (daysBetween) {
        0L -> "Heute, ${date.format(DateTimeFormatter.ofPattern("d. MMMM"))}"
        1L -> "Gestern, ${date.format(DateTimeFormatter.ofPattern("d. MMMM"))}"
        else -> date.format(DateTimeFormatter.ofPattern("E, d. MMMM"))
    }

    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = Dimensions.paddingSmall)
    )
}

private fun List<ActivityLogEntity>.findForHourOnDate(hour: Int, date: LocalDate): ActivityLogEntity? {
    val dateInt = date.year * 10000 + date.monthValue * 100 + date.dayOfMonth
    return this.find { log -> log.hour == hour && log.localDate == dateInt }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HourEntry(
    hour: Int,
    log: ActivityLogEntity?,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onSave: (String, Int, String, Int?, Int?, Int?) -> Unit,
    suggestions: List<String>,
    peopleSuggestions: List<String>,
    cachedData: HourEntryCache?,
    onCacheChanged: (HourEntryCache) -> Unit,
    selectedPreferenceTags: Set<String>
) {
    // Initialize state from either cache or existing log
    val initialChips = cachedData?.activities
        ?: log?.activityText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
        ?: emptyList()
    val initialValence = cachedData?.valence ?: log?.valence ?: 0
    val initialActivityInput = cachedData?.inputText ?: ""
    val initialPeopleChips = cachedData?.peopleChips
        ?: log?.peopleText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
        ?: emptyList()
    val initialPeopleInput = cachedData?.peopleInputText ?: ""
    val initialDurationMinutes = cachedData?.durationMinutes ?: log?.durationMinutes
    val initialDifficultyRating = cachedData?.difficultyRating ?: log?.difficultyRating
    val initialActivationLatencyMinutes = cachedData?.activationLatencyMinutes ?: log?.activationLatencyMinutes

    var activityInputText by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialActivityInput) }
    var activityChips by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialChips) }
    var valence by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialValence) }
    var peopleInputText by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialPeopleInput) }
    var peopleChips by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialPeopleChips) }
    var durationMinutesInput by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialDurationMinutes?.toString().orEmpty()) }
    var difficultyRatingInput by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialDifficultyRating?.toString().orEmpty()) }
    var activationLatencyInput by remember(hour, isExpanded, log, cachedData) { mutableStateOf(initialActivationLatencyMinutes?.toString().orEmpty()) }
    var advancedObservationExpanded by remember(hour, isExpanded, log, cachedData) { mutableStateOf(false) }
    var showCriteriaGuidelines by remember(hour, isExpanded, log, cachedData) { mutableStateOf(false) }
    var hourClassificationTags by remember(hour, isExpanded, log, cachedData) { mutableStateOf(selectedPreferenceTags.intersect(setOf("Routine", "Aktive Handlung"))) }

    // Synchronize state when underlying data changes (e.g., after app restart or data refresh)
    LaunchedEffect(log, cachedData, isExpanded) {
        if (isExpanded) {
            activityInputText = initialActivityInput
            activityChips = initialChips
            valence = initialValence
            peopleInputText = initialPeopleInput
            peopleChips = initialPeopleChips
            durationMinutesInput = initialDurationMinutes?.toString().orEmpty()
            difficultyRatingInput = initialDifficultyRating?.toString().orEmpty()
            activationLatencyInput = initialActivationLatencyMinutes?.toString().orEmpty()
        }
    }

    fun parseOptionalInt(value: String): Int? = value.trim().toIntOrNull()

    val updateCache = {
        onCacheChanged(
            HourEntryCache(
                activities = activityChips,
                valence = valence,
                inputText = activityInputText,
                peopleText = "",
                peopleChips = peopleChips,
                peopleInputText = peopleInputText,
                durationMinutes = parseOptionalInt(durationMinutesInput),
                difficultyRating = parseOptionalInt(difficultyRatingInput),
                activationLatencyMinutes = parseOptionalInt(activationLatencyInput)
            )
        )
    }

    val addActivityChip = {
        val textToAdd = activityInputText.trim()
        if (textToAdd.isNotBlank() && activityChips.size < 3 && !activityChips.contains(textToAdd)) {
            activityChips = activityChips + textToAdd
            activityInputText = ""
            updateCache()
        }
    }

    val addPeopleChip = {
        val textToAdd = peopleInputText.trim()
        if (textToAdd.isNotBlank() && peopleChips.size < 3 && !peopleChips.contains(textToAdd)) {
            peopleChips = peopleChips + textToAdd
            peopleInputText = ""
            updateCache()
        }
    }

    val handleSave = {
        if (activityChips.isNotEmpty()) {
            val activityText = activityChips.joinToString(", ")
            val peopleText = if (peopleChips.isNotEmpty()) peopleChips.joinToString(", ") else ""
            onSave(
                activityText,
                valence,
                peopleText,
                parseOptionalInt(durationMinutesInput),
                parseOptionalInt(difficultyRatingInput),
                parseOptionalInt(activationLatencyInput)
            )
            // Clear the cache immediately to prevent stale data
            onCacheChanged(HourEntryCache())
            // Clear the local input fields immediately after saving
            activityInputText = ""
            activityChips = emptyList()
            valence = 0
            peopleInputText = ""
            peopleChips = emptyList()
            durationMinutesInput = ""
            difficultyRatingInput = ""
            activationLatencyInput = ""
            // Collapse after save
            onExpand()
        }
    }

    val handleCancel = {
        // Reset to original state and collapse
        activityInputText = initialActivityInput
        activityChips = initialChips
        valence = initialValence
        peopleInputText = initialPeopleInput
        peopleChips = initialPeopleChips
        durationMinutesInput = initialDurationMinutes?.toString().orEmpty()
        difficultyRatingInput = initialDifficultyRating?.toString().orEmpty()
        activationLatencyInput = initialActivationLatencyMinutes?.toString().orEmpty()
        onExpand()
    }

    val isSavedAndCollapsed = log != null && !isExpanded
    val cardAlpha = if (isSavedAndCollapsed) 0.6f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onExpand)
            .padding(Dimensions.paddingMedium)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                String.format(Locale.getDefault(), "%02d:00 - %02d:59", hour, hour),
                style = MaterialTheme.typography.titleMedium
            )
            if (isSavedAndCollapsed) {
                Text(
                    text = log.activityText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimensions.paddingMedium)
                )
                Text(
                    valenceLabel(log.valence),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
                modifier = Modifier.padding(top = Dimensions.paddingMedium)
            ) {
                // Activities Section
                if (activityChips.size < 3) {
                    OutlinedTextField(
                        value = activityInputText,
                        onValueChange = { newText ->
                            val filteredText = newText.replace("\n", "")
                            activityInputText = filteredText
                            updateCache()
                            // Auto-chip on separator
                            val lastChar = filteredText.lastOrNull()
                            if ((lastChar == ',' || lastChar == ';') && filteredText.length > 1) {
                                val chipText = filteredText.substring(0, filteredText.length - 1).trim()
                                if (chipText.isNotBlank() && activityChips.size < 3 && !activityChips.contains(chipText)) {
                                    activityChips = activityChips + chipText
                                    activityInputText = ""
                                    updateCache()
                                }
                            }
                        },
                        label = { Text("Hauptaktivitäten der Stunde") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { addActivityChip() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (!it.isFocused) addActivityChip() }
                    )

                    val filteredSuggestions = suggestions.filter {
                        it.contains(activityInputText, ignoreCase = true) &&
                        activityInputText.isNotBlank() &&
                        !activityChips.contains(it)
                    }
                    if (filteredSuggestions.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.heightIn(max = 120.dp)) {
                            items(filteredSuggestions) { suggestion ->
                                ListItem(
                                    headlineContent = { Text(suggestion, style = MaterialTheme.typography.bodySmall) },
                                    modifier = Modifier
                                        .clickable {
                                            if (activityChips.size < 3 && !activityChips.contains(suggestion)) {
                                                activityChips = activityChips + suggestion
                                                activityInputText = ""
                                                updateCache()
                                            }
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (activityChips.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
                    ) {
                        activityChips.forEach { chipText ->
                            InputChip(
                                label = { Text(chipText, style = MaterialTheme.typography.bodySmall) },
                                selected = false,
                                onClick = { /* Nothing to do on click */ },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Aktivität entfernen",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                activityChips = activityChips - chipText
                                                updateCache()
                                            }
                                    )
                                }
                            )
                        }
                    }
                }

                if (activityChips.size >= 3) {
                    Text(
                        text = "3 Aktivitäten erfasst",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = if (showCriteriaGuidelines) "Kriterien ausblenden" else "Kriterien anzeigen",
                    style = MaterialTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showCriteriaGuidelines = !showCriteriaGuidelines }
                )
                AnimatedVisibility(showCriteriaGuidelines) {
                    Text(
                        text = "Orientierung: Aktivität kann Eigeninitiative, ein kleiner Schritt oder bewusst ausgeführte Routine sein. Keine Selbstabwertung – jede ehrliche Eintragung ist hilfreich.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val loggingTypeTags = listOf("Routine", "Aktive Handlung")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    loggingTypeTags.forEach { tag ->
                        FilterChip(
                            selected = hourClassificationTags.contains(tag),
                            onClick = {
                                hourClassificationTags = hourClassificationTags.toMutableSet().apply {
                                    if (contains(tag)) remove(tag) else add(tag)
                                }
                            },
                            label = { Text(tag) }
                        )
                    }
                }
                Text(
                    text = "Freiwillige Einordnung zur Orientierung – Speichern funktioniert weiterhin wie gewohnt.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Valence selection
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Wie ist deine Stimmung?",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val valenceOptions = listOf(
                            -2 to "- -",
                            -1 to "-",
                            0 to "0",
                            1 to "+",
                            2 to "+ +"
                        )
                        valenceOptions.forEach { (v, symbol) ->
                            Button(
                                onClick = {
                                    valence = v
                                    updateCache()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (valence == v) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = if (valence == v) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text(symbol, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                // People section
                if (peopleChips.size < 3) {
                    OutlinedTextField(
                        value = peopleInputText,
                        onValueChange = { newText ->
                            val filteredText = newText.replace("\n", "")
                            peopleInputText = filteredText
                            updateCache()
                            // Auto-chip on separator
                            val lastChar = filteredText.lastOrNull()
                            if ((lastChar == ',' || lastChar == ';') && filteredText.length > 1) {
                                val chipText = filteredText.substring(0, filteredText.length - 1).trim()
                                if (chipText.isNotBlank() && peopleChips.size < 3 && !peopleChips.contains(chipText)) {
                                    peopleChips = peopleChips + chipText
                                    peopleInputText = ""
                                    updateCache()
                                }
                            }
                        },
                        label = { Text("Wichtigste Personen/Gruppen in dieser Stunde") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { addPeopleChip() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (!it.isFocused) addPeopleChip() }
                    )

                    val filteredPeopleSuggestions = peopleSuggestions.filter {
                        it.contains(peopleInputText, ignoreCase = true) &&
                        peopleInputText.isNotBlank() &&
                        !peopleChips.contains(it)
                    }
                    if (filteredPeopleSuggestions.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.heightIn(max = 120.dp)) {
                            items(filteredPeopleSuggestions) { suggestion ->
                                ListItem(
                                    headlineContent = { Text(suggestion, style = MaterialTheme.typography.bodySmall) },
                                    modifier = Modifier
                                        .clickable {
                                            if (peopleChips.size < 3 && !peopleChips.contains(suggestion)) {
                                                peopleChips = peopleChips + suggestion
                                                peopleInputText = ""
                                                updateCache()
                                            }
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (peopleChips.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
                    ) {
                        peopleChips.forEach { chipText ->
                            InputChip(
                                label = { Text(chipText, style = MaterialTheme.typography.bodySmall) },
                                selected = false,
                                onClick = { /* Nothing to do on click */ },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Person entfernen",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                peopleChips = peopleChips - chipText
                                                updateCache()
                                            }
                                    )
                                }
                            )
                        }
                    }
                }

                if (peopleChips.size >= 3) {
                    Text(
                        text = "3 Personen/Gruppen erfasst",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(
                    onClick = { advancedObservationExpanded = !advancedObservationExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Erweiterte Beobachtung (optional)")
                }

                AnimatedVisibility(visible = advancedObservationExpanded) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = durationMinutesInput,
                            onValueChange = { value ->
                                durationMinutesInput = value.filter { it.isDigit() }
                                updateCache()
                            },
                            label = { Text("Dauer (Minuten)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = difficultyRatingInput,
                            onValueChange = { value ->
                                difficultyRatingInput = value.filter { it.isDigit() }
                                updateCache()
                            },
                            label = { Text("Schwierigkeit (0-10)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        OutlinedTextField(
                            value = activationLatencyInput,
                            onValueChange = { value ->
                                activationLatencyInput = value.filter { it.isDigit() }
                                updateCache()
                            },
                            label = { Text("Aktivierungslatenz (Minuten)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )
                    }
                }

                // Save/Cancel buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimensions.spacingSmall),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
                ) {
                    FilledTonalButton(
                        onClick = handleCancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Abbrechen")
                    }
                    Button(
                        onClick = handleSave,
                        modifier = Modifier.weight(1f),
                        enabled = activityChips.isNotEmpty()
                    ) {
                        Text("Speichern")
                    }
                }
            }
        }
    }
}

private fun valenceLabel(valence: Int): String {
    return when (valence) {
        -2 -> "Sehr belastend"
        -1 -> "Eher belastend"
        0 -> "Neutral"
        1 -> "Eher hilfreich"
        2 -> "Sehr hilfreich"
        else -> "Neutral"
    }
}
