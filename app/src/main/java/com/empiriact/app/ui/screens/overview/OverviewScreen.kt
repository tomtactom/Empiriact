package com.empiriact.app.ui.screens.overview

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.empiriact.app.EmpiriactApplication
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.R
import com.empiriact.app.ui.common.getExerciseDisplayName
import com.empiriact.app.ui.common.UiConstants
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.common.getExerciseRoute
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Analysis Types for extensibility
sealed class AnalysisType {
    data class MoodBoosters(val data: List<ActivityAnalysis>) : AnalysisType()
    data class MoodDampers(val data: List<ActivityAnalysis>) : AnalysisType()
    data class ActivityFrequencyAnalysis(val data: List<ActivityFrequency>) : AnalysisType()
    data class ValenceTrends(val data: List<ValenceTrend>) : AnalysisType()
}

data class AnalysisItem(
    val type: AnalysisType,
    val title: String,
    val emptyMessage: String,
    val icon: ImageVector,
    val iconTint: Color
)

private sealed class ProtocolScreenItem {
    data class Header(val date: LocalDate) : ProtocolScreenItem()
    data class LogRow(val row: ProtocolLogUiModel) : ProtocolScreenItem()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(
    factory: ViewModelFactory,
    navController: NavController
) {
    val vm: OverviewViewModel = viewModel(factory = factory)

    val application = LocalContext.current.applicationContext as EmpiriactApplication

    LaunchedEffect(Unit) {
        application.passiveSnapshotRefresher.refreshSnapshot()
    }

    val tabs = listOf(
        stringResource(R.string.overview_tab_protocol),
        stringResource(R.string.overview_tab_analysis),
        stringResource(R.string.overview_tab_resources)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ProtocolTab(vm)
                1 -> AnalysisTab(vm)
                2 -> ResourcesTab(vm, navController)
            }
        }
    }
}

@Composable
private fun AnalysisTab(vm: OverviewViewModel) {
    val moodBoosters by vm.moodBoosters.collectAsState()
    val moodDampers by vm.moodDampers.collectAsState()
    val activityFrequency by vm.activityFrequency.collectAsState()
    val valenceTrends by vm.valenceTrends.collectAsState()

    Log.d("AnalysisTab", "Rendering with data sizes: boosters=${moodBoosters.size}, dampers=${moodDampers.size}, freq=${activityFrequency.size}, trends=${valenceTrends.size}")

    // Loading state - show loading until data is available
    val isLoading = moodBoosters.isEmpty() && moodDampers.isEmpty() && activityFrequency.isEmpty() && valenceTrends.isEmpty()

    // Define strings and colors outside remember to avoid @Composable invocations in non-@Composable context
    val moodBoostersTitle = stringResource(R.string.analysis_tab_mood_boosters_title)
    val moodBoostersEmpty = stringResource(R.string.analysis_tab_mood_boosters_empty)
    val moodDampersTitle = stringResource(R.string.analysis_tab_mood_dampers_title)
    val moodDampersEmpty = stringResource(R.string.analysis_tab_mood_dampers_empty)
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    // Build analysis items dynamically - this makes it easy to add new analysis types
    val analysisItems = listOf(
        AnalysisItem(
            type = AnalysisType.MoodBoosters(moodBoosters),
            title = moodBoostersTitle,
            emptyMessage = moodBoostersEmpty,
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            iconTint = UiConstants.POSITIVE_CHANGE_COLOR
        ),
        AnalysisItem(
            type = AnalysisType.MoodDampers(moodDampers),
            title = moodDampersTitle,
            emptyMessage = moodDampersEmpty,
            icon = Icons.AutoMirrored.Filled.TrendingDown,
            iconTint = UiConstants.NEGATIVE_CHANGE_COLOR
        ),
        AnalysisItem(
            type = AnalysisType.ActivityFrequencyAnalysis(activityFrequency),
            title = "Aktivitäts Häufigkeit",
            emptyMessage = "Keine Daten verfügbar",
            icon = Icons.Default.BarChart,
            iconTint = primaryColor
        ),
        AnalysisItem(
            type = AnalysisType.ValenceTrends(valenceTrends),
            title = "Valenz Trends",
            emptyMessage = "Keine Daten verfügbar",
            icon = Icons.Default.Timeline,
            iconTint = secondaryColor
        )
    )

    Log.d("AnalysisTab", "analysisItems built: ${analysisItems.size} items")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(UiConstants.PADDING_LARGE),
        verticalArrangement = Arrangement.spacedBy(UiConstants.ARRANGEMENT_SPACING_LARGE)
    ) {
        item {
            Text(
                text = stringResource(R.string.analysis_tab_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.analysis_tab_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isLoading) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Analysen werden berechnet...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Render analysis items dynamically
            items(analysisItems, key = { it.title }) { analysisItem ->
                AnalysisItemRenderer(analysisItem)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProtocolTab(vm: OverviewViewModel) {
    val dayLogs by vm.dayLogs.collectAsState()

    val protocolItems = remember(dayLogs) {
        dayLogs.groupBy { it.activityLog.localDate }
            .toSortedMap(compareByDescending { it })
            .flatMap {
                val date = LocalDate.of(it.key / 10000, (it.key / 100) % 100, it.key % 100)
                listOf(ProtocolScreenItem.Header(date)) + it.value.map { log -> ProtocolScreenItem.LogRow(log) }
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(UiConstants.PADDING_LARGE),
        verticalArrangement = Arrangement.spacedBy(UiConstants.ARRANGEMENT_SPACING_MEDIUM)
    ) {
        if (protocolItems.isEmpty()) {
            item {
                Text(stringResource(R.string.protocol_tab_empty), modifier = Modifier.padding(top = UiConstants.PADDING_LARGE))
            }
        } else {
            item {
                ProtocolColumnHeaderRow()
            }
            items(
                items = protocolItems,
                key = {
                    when (it) {
                        is ProtocolScreenItem.Header -> "header_${it.date.toEpochDay()}"
                        is ProtocolScreenItem.LogRow -> "log_${it.row.activityLog.key}"
                    }
                }
            ) { item ->
                when (item) {
                    is ProtocolScreenItem.Header -> DateHeader(date = item.date)
                    is ProtocolScreenItem.LogRow -> ProtocolRow(item.row)
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
            .padding(vertical = UiConstants.PADDING_SMALL)
    )
}


@Composable
private fun ResourcesTab(vm: OverviewViewModel, navController: NavController) {
    val exerciseRatings by vm.exerciseRatings.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(UiConstants.PADDING_LARGE),
        verticalArrangement = Arrangement.spacedBy(UiConstants.ARRANGEMENT_SPACING_MEDIUM)
    ) {
        item {
            Text(stringResource(R.string.resources_tab_title), style = MaterialTheme.typography.headlineMedium)
        }
        if (exerciseRatings.isEmpty()) {
            item {
                Text(stringResource(R.string.resources_tab_empty), modifier = Modifier.padding(top = UiConstants.PADDING_LARGE))
            }
        } else {
            items(
                items = exerciseRatings,
                key = { it.exerciseId }
            ) { exercise ->
                ExerciseRatingRow(exercise, navController)
            }
        }
    }
}

@Composable
private fun ExerciseRatingRow(exercise: ExerciseWithRating, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                getExerciseRoute(exercise.exerciseId)?.let { route ->
                    navController.navigate(route)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.CARD_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConstants.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getExerciseDisplayName(exercise.exerciseId),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(UiConstants.PADDING_MEDIUM))
                RatingBar(rating = exercise.averageRating)
                Spacer(Modifier.height(UiConstants.PADDING_SMALL))
                Text(
                    text = String.format("Ø %.1f / 5.0", exercise.averageRating.takeIf { !it.isNaN() && !it.isInfinite() } ?: 0.0),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun RatingBar(rating: Double, maxRating: Int = 5) {
    val safeRating = rating.takeIf { !it.isNaN() && !it.isInfinite() } ?: 0.0
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(UiConstants.PADDING_SMALL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxRating) { index ->
            val fillPercentage = when {
                index < safeRating.toInt() -> 1f
                index == safeRating.toInt() -> (safeRating - index).toFloat()
                else -> 0f
            }
            Box(
                modifier = Modifier
                    .size(UiConstants.RATING_BAR_ITEM_SIZE)
                    .background(
                        color = if (fillPercentage > 0) UiConstants.RATING_FILLED_COLOR else UiConstants.RATING_UNFILLED_COLOR,
                        shape = RoundedCornerShape(UiConstants.RATING_BAR_CORNER_SHAPE)
                    )
            )
        }
    }
}

@Composable
private fun ProtocolColumnHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.protocol_column_time),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_TIME_WEIGHT)
        )
        Text(
            text = stringResource(R.string.protocol_column_activity),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_ACTIVITY_WEIGHT)
        )
        Text(
            text = stringResource(R.string.protocol_column_mood),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_VALENCE_WEIGHT)
        )
        Text(
            text = stringResource(R.string.protocol_column_steps),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_STEPS_WEIGHT)
        )
    }
}

@Composable
private fun ProtocolRow(row: ProtocolLogUiModel) {
    val log = row.activityLog
    val stepCountText = protocolStepDisplayText(row.stepDisplayState)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            String.format("%02d:%02d", log.hour, 0),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_TIME_WEIGHT)
        )
        Text(
            log.activityText.ifBlank { "–" },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_ACTIVITY_WEIGHT)
        )
        Text(
            valenceLabel(log.valence),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_VALENCE_WEIGHT)
        )
        Text(
            stepCountText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_STEPS_WEIGHT)
        )
    }
}



@Composable
private fun protocolStepDisplayText(stepDisplayState: StepDisplayState): String {
    return when (stepDisplayState) {
        is StepDisplayState.Recorded -> stepDisplayState.count.toString()
        else -> stringResource(requireNotNull(protocolStepTextRes(stepDisplayState)))
    }
}

internal fun protocolStepTextRes(stepDisplayState: StepDisplayState): Int? =
    when (stepDisplayState) {
        is StepDisplayState.Recorded -> null
        is StepDisplayState.NotRecorded -> when (stepDisplayState.reason) {
            StepDisplayState.NotRecorded.Reason.PERMISSION_MISSING -> R.string.protocol_steps_permission_missing
            StepDisplayState.NotRecorded.Reason.BASELINE_PENDING,
            StepDisplayState.NotRecorded.Reason.UNKNOWN -> R.string.protocol_steps_not_recorded
        }
        StepDisplayState.Disabled -> R.string.protocol_steps_tracking_disabled
    }

@Composable
private fun AnalysisSection(title: String, analyses: List<ActivityAnalysis>, emptyMessage: String, icon: ImageVector, iconTint: Color) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = UiConstants.PADDING_MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(UiConstants.ANALYSIS_SECTION_ICON_SIZE)
            )
            Spacer(modifier = Modifier.width(UiConstants.PADDING_MEDIUM))
            Text(title, style = MaterialTheme.typography.headlineSmall)
        }

        if (analyses.isEmpty()) {
            Text(emptyMessage, style = MaterialTheme.typography.bodyMedium)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM)) {
                analyses.forEach { analysis ->
                    ActivityAnalysisCard(analysis = analysis)
                }
            }
        }
    }
}

@Composable
private fun ActivityAnalysisCard(analysis: ActivityAnalysis) {
    val rating = analysis.averageRating
    val ratingText = if (rating.isNaN() || rating.isInfinite()) "0.0" else String.format("%.1f", rating)
    val color = when {
        rating > 0 -> UiConstants.POSITIVE_CHANGE_COLOR
        rating < 0 -> UiConstants.NEGATIVE_CHANGE_COLOR
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.CARD_ELEVATION)) {
        Row(modifier = Modifier.padding(UiConstants.PADDING_LARGE), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(analysis.activity.ifBlank { "Unbekannte Aktivität" }, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Text(
                text = ratingText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun ActivityFrequencySection(frequencyData: List<ActivityFrequency>, title: String, emptyMessage: String, icon: ImageVector, iconTint: Color) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = UiConstants.PADDING_MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(UiConstants.ANALYSIS_SECTION_ICON_SIZE)
            )
            Spacer(modifier = Modifier.width(UiConstants.PADDING_MEDIUM))
            Text(title, style = MaterialTheme.typography.headlineSmall)
        }
        if (frequencyData.isEmpty()) {
            Text(emptyMessage, style = MaterialTheme.typography.bodyMedium)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM)) {
                frequencyData.forEach { item ->
                    Text("${item.activity}: ${item.count}")
                }
            }
        }
    }
}

@Composable
private fun ValenceTrendsSection(trends: List<ValenceTrend>, title: String, emptyMessage: String, icon: ImageVector, iconTint: Color) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = UiConstants.PADDING_MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(UiConstants.ANALYSIS_SECTION_ICON_SIZE)
            )
            Spacer(modifier = Modifier.width(UiConstants.PADDING_MEDIUM))
            Text(title, style = MaterialTheme.typography.headlineSmall)
        }
        if (trends.isEmpty()) {
            Text(emptyMessage, style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM)
            ) {
                items(trends, key = { it.date.toString() }) { trend ->
                    ValenceTrendItem(trend)
                }
            }
        }
    }
}

@Composable
private fun ValenceTrendItem(trend: ValenceTrend) {
    val valence = trend.averageValence
    val valenceText = if (valence.isNaN() || valence.isInfinite()) "0.0" else String.format("%.1f", valence)
    val color = when {
        valence > 0 -> UiConstants.POSITIVE_CHANGE_COLOR
        valence < 0 -> UiConstants.NEGATIVE_CHANGE_COLOR
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.CARD_ELEVATION)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConstants.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(trend.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), style = MaterialTheme.typography.bodyLarge)
            Text(
                text = valenceText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun AnalysisItemRenderer(analysisItem: AnalysisItem) {
    Log.d("AnalysisItemRenderer", "Rendering item: ${analysisItem.title}")
    when (analysisItem.type) {
        is AnalysisType.MoodBoosters -> {
            AnalysisSection(
                title = analysisItem.title,
                analyses = analysisItem.type.data,
                emptyMessage = analysisItem.emptyMessage,
                icon = analysisItem.icon,
                iconTint = analysisItem.iconTint
            )
        }
        is AnalysisType.MoodDampers -> {
            AnalysisSection(
                title = analysisItem.title,
                analyses = analysisItem.type.data,
                emptyMessage = analysisItem.emptyMessage,
                icon = analysisItem.icon,
                iconTint = analysisItem.iconTint
            )
        }
        is AnalysisType.ActivityFrequencyAnalysis -> {
            ActivityFrequencySection(analysisItem.type.data, analysisItem.title, analysisItem.emptyMessage, analysisItem.icon, analysisItem.iconTint)
        }
        is AnalysisType.ValenceTrends -> {
            Text("ValenceTrends: ${analysisItem.type.data.size} items")
        }
    }
}

private fun valenceLabel(v: Int): String = when (v) {
    -2 -> "--"
    -1 -> "-"
    0 -> "0"
    1 -> "+"
    2 -> "++"
    else -> "–"
}
