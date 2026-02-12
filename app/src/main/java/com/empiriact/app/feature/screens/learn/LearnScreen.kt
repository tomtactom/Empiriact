package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

data class LearningModule(val title: String, val description: String, val route: Route)

private val modules = listOf(
    LearningModule(
        "Grundlagen",
        "Lerne die Grundkonzepte und fundamentalen Prinzipien",
        Route.LearnBasics
    ),
    LearningModule(
        "Fortgeschrittene Techniken",
        "Vertiefe dein Wissen mit fortgeschrittenen Methoden",
        Route.LearnAdvanced
    ),
    LearningModule(
        "Praktische Übungen",
        "Stelle dein Wissen in praktischen Aufgaben unter Beweis",
        Route.LearnPractice
    ),
    LearningModule(
        "Testmodul",
        "Überprüfe dein Wissen mit interaktiven Tests",
        Route.LearnTest
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LearnScreen(navController: NavController) {
    val tabs = listOf("Module", "Mein Fortschritt", "Ressourcen")
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
                0 -> ModulesTab(navController)
                1 -> ProgressTab()
                2 -> ResourcesTab()
            }
        }
    }
}

@Composable
private fun ModulesTab(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lernen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(modules) { module ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(module.route.route) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = module.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dein Fortschritt",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Hier kannst du deinen Lernfortschritt verfolgen.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ResourcesTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ressourcen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Zusätzliche Materialien und externe Ressourcen.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

