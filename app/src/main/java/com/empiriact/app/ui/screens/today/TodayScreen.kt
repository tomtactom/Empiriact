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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val unsavedChanges by vm.unsavedChanges.collectAsState()
    val todayIntroCompleted by vm.todayIntroCompleted.collectAsState()

    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val currentHour = now.hour
    var expandedEntry by remember { mutableStateOf<HourEntryKey?>(null) }
    var showAllHours by remember { mutableStateOf(false) }

    if (expandedEntry == null && logs.findForHourOnDate(currentHour, today) == null) {
        expandedEntry = HourEntryKey(today, currentHour)
    }

    val items = remember(showAllHours, currentHour, today) {
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
                    onStart = {
                        expandedEntry = HourEntryKey(today, currentHour)
                    },
                    onDismiss = { vm.completeTodayIntro() }
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
                        onSave = { activity, valence ->
                            vm.upsertActivityForHour(
                                item.date,
                                item.hour,
                                activity,
                                valence
                            )
                        },
                        suggestions = uniqueActivities,
                        cachedData = unsavedChanges[HourEntryKey(item.date, item.hour)],
                        onCacheChanged = { cache -> vm.cacheHourEntry(item.date, item.hour, cache) }
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
                text = "Du musst nichts perfekt machen. Wir sammeln nur Beobachtungen: 1–3 Hauptaktivitäten pro Stunde und die Stimmung dazu.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tipp: Trage direkt nach der Stunde ein. Auch Routinen oder \"wenig produktive\" Zeiten sind wertvolle Hinweise.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
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
    onSave: (String, Int) -> Unit,
    suggestions: List<String>,
    cachedData: HourEntryCache?,
    onCacheChanged: (HourEntryCache) -> Unit
) {
    var activityInputText by remember { mutableStateOf("") }
    var activityChips by remember { mutableStateOf<List<String>>(emptyList()) }
    var valence by remember { mutableStateOf(0) }

    val addChipFromInput = {
        val textToAdd = activityInputText.trim()
        if (textToAdd.isNotBlank() && activityChips.size < 3 && !activityChips.contains(textToAdd)) {
            val newChips = activityChips + textToAdd
            activityChips = newChips
            activityInputText = ""
            onCacheChanged(HourEntryCache(newChips, valence, ""))
        }
    }

    // When the view is expanded to edit, populate chips from the existing log or cache.
    LaunchedEffect(hour, isExpanded, log, cachedData) {
        if (isExpanded) {
            val initialChips = cachedData?.activities ?: log?.activityText?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
            val initialValence = cachedData?.valence ?: log?.valence ?: 0
            val initialInput = cachedData?.inputText ?: ""
            activityChips = initialChips
            valence = initialValence
            activityInputText = initialInput
        } else {
            activityChips = emptyList()
            activityInputText = ""
            valence = 0
        }
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
                String.format("%02d:00 - %02d:59", hour, hour),
                style = MaterialTheme.typography.titleMedium
            )
            if (isSavedAndCollapsed) {
                Text(
                    text = log!!.activityText,
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
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge),
                modifier = Modifier.padding(top = Dimensions.paddingLarge)
            ) {
                Text(
                    text = "Fokussiere dich auf 1–3 Hauptaktivitäten dieser Stunde. Es geht um Beobachtung, nicht Bewertung.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (activityChips.size < 3) {
                    OutlinedTextField(
                        value = activityInputText,
                        onValueChange = { newText ->
                            activityInputText = newText
                            onCacheChanged(HourEntryCache(activityChips, valence, newText))
                            val lastChar = newText.lastOrNull()
                            if ((lastChar == ',' || lastChar == ';') && newText.length > 1) {
                                val chipText = newText.substring(0, newText.length - 1).trim()
                                if (chipText.isNotBlank() && activityChips.size < 3 && !activityChips.contains(chipText)) {
                                    val newChips = activityChips + chipText
                                    activityChips = newChips
                                    activityInputText = ""
                                    onCacheChanged(HourEntryCache(newChips, valence, ""))
                                }
                            }
                        },
                        label = { Text("Was war heute in dieser Stunde am wichtigsten?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (!it.isFocused) addChipFromInput() }
                    )

                    val filteredSuggestions = suggestions.filter { it.contains(activityInputText, ignoreCase = true) && activityInputText.isNotBlank() && !activityChips.contains(it) }
                    if (filteredSuggestions.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                            items(filteredSuggestions) { suggestion ->
                                ListItem(
                                    headlineContent = { Text(suggestion) },
                                    modifier = Modifier.clickable {
                                        if (activityChips.size < 3 && !activityChips.contains(suggestion)) {
                                            val newChips = activityChips + suggestion
                                            activityChips = newChips
                                            activityInputText = ""
                                            onCacheChanged(HourEntryCache(newChips, valence, ""))
                                        }
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Sie können bis zu 3 Aktivitäten hinzufügen.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = Dimensions.paddingMedium, vertical = Dimensions.paddingSmall)
                    )
                }

                if (activityChips.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
                    ) {
                        activityChips.forEach { chipText ->
                            InputChip(
                                label = { Text(chipText) },
                                selected = false,
                                onClick = { /* Nothing to do on click */ },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Aktivität entfernen",
                                        modifier = Modifier
                                            .size(InputChipDefaults.IconSize)
                                            .clickable {
                                                val newChips = activityChips - chipText
                                                activityChips = newChips
                                                onCacheChanged(HourEntryCache(newChips, valence, activityInputText))
                                            }
                                    )
                                }
                            )
                        }
                    }
                }

                ValencePicker(selectedValence = valence, onValenceSelected = {
                    valence = it
                    onCacheChanged(HourEntryCache(activityChips, it, activityInputText))
                })
                Button(
                    onClick = {
                        addChipFromInput()
                        val finalText = activityChips.joinToString(", ")
                        onSave(finalText, valence)
                        onExpand() // Collapse after save
                    },
                    enabled = activityInputText.isNotBlank() || activityChips.isNotEmpty(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Eintrag speichern")
                }
            }
        }
    }
}

@Composable
private fun ValencePicker(selectedValence: Int, onValenceSelected: (Int) -> Unit) {
    val valences = listOf(-2, -1, 0, 1, 2)
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
    ) {
        valences.forEach { v ->
            val isSelected = selectedValence == v
            FilledTonalButton(
                onClick = { onValenceSelected(v) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(valenceLabel(v))
            }
        }
    }
}

private fun valenceLabel(v: Int): String = when (v) {
    -2 -> "Sehr belastend"
    -1 -> "Eher belastend"
    0 -> "Neutral"
    1 -> "Eher hilfreich"
    2 -> "Sehr hilfreich"
    else -> "Neutral"
}
