package com.empiriact.app.ui.screens.checkin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.empiriact.app.data.LikertScale
import com.empiriact.app.data.QuestionnaireItem
import com.empiriact.app.data.ScoreInterpretation
import com.empiriact.app.data.WellbeingQuestionnaire
import com.empiriact.app.ui.theme.Dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuestionnaireDetailScreen(questionnaire: WellbeingQuestionnaire, onBack: () -> Unit) {
    val totalPages = questionnaire.items.size + 1 // +1 for the initial instruction screen
    val pagerState = rememberPagerState(pageCount = { totalPages })
    var answers by remember { mutableStateOf(mapOf<String, Int>()) }
    val coroutineScope = rememberCoroutineScope()

    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(questionnaire.title) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Zurück") } }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (!showResult) {
                val progress by animateFloatAsState(
                    targetValue = (pagerState.currentPage + 1) / totalPages.toFloat(),
                    label = "ProgressAnimation"
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showResult) {
                val totalScore = answers.values.sum()
                val interpretation = questionnaire.interpretations.firstOrNull { totalScore in it.scoreRange }
                if (interpretation != null) {
                    ResultView(interpretation, onBack)
                } else {
                    FallbackResultView(onBack)
                }
            } else {
                HorizontalPager(state = pagerState, userScrollEnabled = answers.size >= pagerState.currentPage) { page ->
                    when (page) {
                        0 -> InstructionView(questionnaire) {
                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                        }
                        else -> {
                            val itemIndex = page - 1
                            val currentItem = questionnaire.items[itemIndex]
                            QuestionView(
                                item = currentItem,
                                scale = questionnaire.scale,
                                answer = answers[currentItem.id],
                                onAnswer = { answer -> answers = answers + (currentItem.id to answer) },
                                onNext = {
                                    if (itemIndex < questionnaire.items.size - 1) {
                                        coroutineScope.launch { pagerState.animateScrollToPage(page + 1) }
                                    } else {
                                        showResult = true
                                    }
                                },
                                pageIndicator = "${itemIndex + 1} von ${questionnaire.items.size}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InstructionView(questionnaire: WellbeingQuestionnaire, onStart: () -> Unit) {
    Column(modifier = Modifier.padding(Dimensions.paddingMedium).fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(text = questionnaire.instruction, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) { Text("Starten") }
    }
}

@Composable
private fun QuestionView(
    item: QuestionnaireItem,
    scale: LikertScale,
    answer: Int?,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit,
    pageIndicator: String
) {
    Column(modifier = Modifier.padding(Dimensions.paddingMedium).fillMaxSize()) {
        Text(text = pageIndicator, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
        Text(text = item.text, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        (1..scale.steps).forEach { step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnswer(step) }
                    .padding(vertical = Dimensions.paddingSmall)
            ) {
                RadioButton(
                    selected = answer == step,
                    onClick = { onAnswer(step) }
                )
                val anchorText = when (step) {
                    1 -> scale.lowAnchor
                    scale.steps -> scale.highAnchor
                    else -> ""
                }
                Text(text = anchorText, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, enabled = answer != null, modifier = Modifier.fillMaxWidth()) { Text("Weiter") }
    }
}

@Composable
private fun ResultView(interpretation: ScoreInterpretation, onFinish: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(Dimensions.paddingMedium)) {
        item {
            Text(text = "Auswertung", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
        }
        item {
            Card(elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.elevationSmall)) {
                Column(modifier = Modifier.padding(Dimensions.paddingLarge)) {
                    Text(text = interpretation.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    Text(text = interpretation.interpretation, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
                    Text(text = "Empfehlung", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    Text(text = interpretation.recommendation, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) { Text("Abschließen") }
        }
    }
}


@Composable
private fun FallbackResultView(onFinish: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(Dimensions.paddingMedium)) {
        item {
            Text(text = "Auswertung", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            Text(text = "Für deine Antworten liegt aktuell noch keine passende Interpretation vor.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) { Text("Abschließen") }
        }
    }
}
