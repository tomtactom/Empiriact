package com.empiriact.app.ui.screens.overview

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.R
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.ui.common.getExerciseDisplayName
import com.empiriact.app.ui.common.UiConstants
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.common.getExerciseRoute
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private sealed class ProtocolScreenItem {
    data class Header(val date: LocalDate) : ProtocolScreenItem()
    data class LogRow(val log: ActivityLogEntity) : ProtocolScreenItem()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(
    factory: ViewModelFactory,
    navController: NavController
) {
    val vm: OverviewViewModel = viewModel(factory = factory)

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

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(UiConstants.PADDING_LARGE),
        verticalArrangement = Arrangement.spacedBy(UiConstants.ARRANGEMENT_SPACING_LARGE)
    ) {
        item {
            Text(stringResource(R.string.analysis_tab_title), style = MaterialTheme.typography.headlineMedium)
            Text(stringResource(R.string.analysis_tab_subtitle), style = MaterialTheme.typography.bodyMedium)
        }
        item { AnalysisSection(title = stringResource(R.string.analysis_tab_mood_boosters_title), analyses = moodBoosters, emptyMessage = stringResource(R.string.analysis_tab_mood_boosters_empty)) }
        item { AnalysisSection(title = stringResource(R.string.analysis_tab_mood_dampers_title), analyses = moodDampers, emptyMessage = stringResource(R.string.analysis_tab_mood_dampers_empty)) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProtocolTab(vm: OverviewViewModel) {
    val dayLogs by vm.dayLogs.collectAsState()

    val protocolItems = remember(dayLogs) {
        dayLogs.groupBy { it.localDate }
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
            items(
                items = protocolItems,
                key = {
                    when (it) {
                        is ProtocolScreenItem.Header -> "header_${it.date.toEpochDay()}"
                        is ProtocolScreenItem.LogRow -> "log_${it.log.key}"
                    }
                }
            ) { item ->
                when (item) {
                    is ProtocolScreenItem.Header -> DateHeader(date = item.date)
                    is ProtocolScreenItem.LogRow -> ProtocolRow(item.log)
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
                    text = String.format("Ø %.1f / 5.0", exercise.averageRating),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun RatingBar(rating: Double, maxRating: Int = 5) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(UiConstants.PADDING_SMALL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxRating) { index ->
            val fillPercentage = when {
                index < rating.toInt() -> 1f
                index == rating.toInt() -> (rating - index).toFloat()
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
private fun ProtocolRow(log: ActivityLogEntity) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(String.format("%02d:%02d", log.hour, 0), style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_TIME_WEIGHT))
        Text(log.activityText, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_ACTIVITY_WEIGHT))
        Text(valenceLabel(log.valence), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(UiConstants.PROTOCOL_ROW_VALENCE_WEIGHT))
    }
}


@Composable
private fun AnalysisSection(title: String, analyses: List<ActivityAnalysis>, emptyMessage: String) {
    Column {
        Text(title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = UiConstants.PADDING_MEDIUM))
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
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = UiConstants.CARD_ELEVATION)) {
        Row(modifier = Modifier.padding(UiConstants.PADDING_LARGE), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(analysis.activity, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Text(
                text = String.format("%.1f", analysis.averageRating),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (analysis.averageRating > 0) UiConstants.POSITIVE_CHANGE_COLOR else UiConstants.NEGATIVE_CHANGE_COLOR
            )
        }
    }
}

private fun valenceLabel(v: Int): String = when (v) {
    -2 -> "--"
    -1 -> "-"
    0 -> "0"
    1 -> "+"
    2 -> "++"
    else -> "0"
}
