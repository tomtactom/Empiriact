package com.empiriact.app.ui.screens.checkin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.data.Questionnaire
import com.empiriact.app.data.QuestionnaireState
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.navigation.Route
import com.empiriact.app.ui.theme.Dimensions

@Composable
fun CheckinScreen(factory: ViewModelFactory, navController: NavController) {
    val vm: CheckinViewModel = viewModel(factory = factory)
    val questionnaires by vm.questionnaires.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
        contentPadding = PaddingValues(vertical = Dimensions.paddingMedium)
    ) {
        item {
            Text(
                "Check-in",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        items(questionnaires, key = { it.id }) { questionnaire ->
            QuestionnaireItem(questionnaire) {
                if (questionnaire.state == QuestionnaireState.ON) {
                    navController.navigate(Route.QuestionnaireDetail.createRoute(questionnaire.id))
                }
            }
        }
    }
}

@Composable
private fun QuestionnaireItem(questionnaire: Questionnaire, onClick: () -> Unit) {
    val isPaused = questionnaire.state == QuestionnaireState.PAUSED
    val cardAlpha = if (isPaused) 0.6f else 1.0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.elevationSmall)
    ) {
        Column(modifier = Modifier.padding(Dimensions.paddingLarge)) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)) {
                if (questionnaire.icon != null) {
                    Icon(imageVector = questionnaire.icon, contentDescription = null)
                }
                Text(
                    text = questionnaire.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            Text(
                text = questionnaire.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
