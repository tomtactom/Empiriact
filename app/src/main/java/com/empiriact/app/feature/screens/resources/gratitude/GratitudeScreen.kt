package com.empiriact.app.ui.screens.resources.gratitude

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.ui.common.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val gratitudePrompts = listOf(
    "What is a small gesture that you appreciated today?",
    "Who is someone in your life you are grateful for, and why?",
    "What is something beautiful you saw today?",
    "What is a skill you are grateful to have?",
    "What is a simple pleasure that brought you joy today?"
)

@Composable
fun GratitudeScreen(factory: ViewModelFactory) {
    val vm: GratitudeViewModel = viewModel(factory = factory)
    val gratitudeLog by vm.getGratitudeLogForDate(LocalDate.now()).collectAsState()
    var text by remember { mutableStateOf("") }
    val prompt = remember { gratitudePrompts.random() }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(prompt, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Tell me more...") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { vm.saveGratitudeEntry(text, LocalDate.now()) }) {
                        Text("Save")
                    }
                }
            }
        }

        item {
            AnimatedVisibility(visible = gratitudeLog != null) {
                gratitudeLog?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Your Past Entries", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        gratitudeLog?.let {
            item {
                GratitudeLogItem(it.entry, it.date)
            }
        }
    }
}

@Composable
private fun GratitudeLogItem(entry: String, date: LocalDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(entry, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}
