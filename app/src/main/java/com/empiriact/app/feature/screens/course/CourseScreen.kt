package com.empiriact.app.ui.screens.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.empiriact.app.R

data class Course(val title: String, val description: String)

val placeholderCourses = listOf(
    Course("Einführung in die Selbstreflexion", "Lerne die Grundlagen, um deine Gedanken und Gefühle zu verstehen."),
    Course("Muster erkennen", "Entdecke wiederkehrende Muster in deinem täglichen Leben."),
    Course("Ziele setzen und erreichen", "Formuliere klare Ziele und verfolge sie mit neuem Fokus."),
    Course("Achtsamkeit im Alltag", "Integriere kleine Achtsamkeitsübungen in deinen Tag.")
)

@Composable
fun CourseScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.illustration_course_hero),
                contentDescription = "Illustration of a plant growing out of a book",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(vertical = 16.dp)
            )
            Text(
                text = "Dein Weg zu mehr Einsicht",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(placeholderCourses) { course ->
            CourseCard(course = course)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { /* TODO: Handle course click */ }
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
