package com.empiriact.app.ui.screens.reflect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.Routine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectScreen() {
    val application = LocalContext.current.applicationContext as EmpiriactApplication
    val vm: ReflectViewModel = viewModel(factory = application.viewModelFactory)
    val routines by vm.routines.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Routine")
            }
        }
    ) {
        Column(Modifier.fillMaxSize().padding(it).padding(16.dp)) {
            Text("Reflexion", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))

            if (showDialog) {
                AddRoutineDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { name, description ->
                        vm.addRoutine(name, description)
                        showDialog = false
                    }
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(routines) { routine ->
                    RoutineItem(routine)
                }
            }
        }
    }
}

@Composable
private fun RoutineItem(routine: Routine) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(routine.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(routine.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun AddRoutineDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neue Routine hinzufügen") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, description) }) {
                Text("Hinzufügen")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}
