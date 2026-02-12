package com.empiriact.app.ui.screens.values

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.data.UserValue
import com.empiriact.app.data.ValueCategory
import com.empiriact.app.ui.navigation.Route
import kotlin.math.roundToInt

@Composable
fun ValuesScreen(
    navController: NavController,
    viewModel: ValuesViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Meine Werte", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(8.dp))
                Text("Bewerte, wie wichtig dir die folgenden Lebensbereiche sind und wie sehr du sie in der letzten Woche umgesetzt hast.", style = MaterialTheme.typography.bodyMedium)
            }
            items(categories) { category ->
                ValueCategorySection(navController, category, viewModel)
            }
        }
    }
}

@Composable
fun ValueCategorySection(
    navController: NavController,
    category: ValueCategory,
    viewModel: ValuesViewModel
) {
    Column {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        category.values.forEach { value ->
            ValueCard(
                value = value,
                onClick = { navController.navigate(Route.ActivityPlanner.createRoute(value.description)) },
                onImportanceChange = { newValue -> viewModel.updateImportance(value.name, newValue) },
                onImplementationChange = { newValue -> viewModel.updateImplementation(value.name, newValue) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ValueCard(
    value: UserValue,
    onClick: () -> Unit,
    onImportanceChange: (Int) -> Unit,
    onImplementationChange: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value.description, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            ValueSlider(label = "Wichtigkeit", value = value.importance, onValueChange = onImportanceChange)
            Spacer(Modifier.height(16.dp))
            ValueSlider(label = "Umsetzung (letzte Woche)", value = value.implementation, onValueChange = onImplementationChange)
        }
    }
}

@Composable
fun ValueSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(value.toString(), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = 0f..10f,
            steps = 9
        )
    }
}
