package com.empiriact.app.ui.screens.evaluations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication

@Composable
fun TakeEvaluationScreen(type: String, onEvaluationComplete: () -> Unit) {
    val application = LocalContext.current.applicationContext as EmpiriactApplication
    val vm: EvaluationViewModel = viewModel(factory = application.viewModelFactory)
    val questionnaires = remember {
        listOf(
            Questionnaire("Depressive Symptome", listOf(
                Question("Ich habe wenig Interesse oder Freude an Dingen", listOf("Überhaupt nicht", "An einzelnen Tagen", "An mehr als der Hälfte der Tage", "Beinahe jeden Tag")),
                Question("Ich fühle mich niedergeschlagen, deprimiert oder hoffnungslos", listOf("Überhaupt nicht", "An einzelnen Tagen", "An mehr als der Hälfte der Tage", "Beinahe jeden Tag")),
                Question("Ich fühle mich müde oder energielos", listOf("Überhaupt nicht", "An einzelnen Tagen", "An mehr als der Hälfte der Tage", "Beinahe jeden Tag")),
            )),
            Questionnaire("Lebenszufriedenheit", listOf(
                Question("Mit meinem Leben bin ich im Großen und Ganzen zufrieden", listOf("Stimme überhaupt nicht zu", "Stimme nicht zu", "Stimme eher nicht zu", "Weder noch", "Stimme eher zu", "Stimme zu", "Stimme voll und ganz zu")),
                Question("Meine Lebensbedingungen sind ausgezeichnet", listOf("Stimme überhaupt nicht zu", "Stimme nicht zu", "Stimme eher nicht zu", "Weder noch", "Stimme eher zu", "Stimme zu", "Stimme voll und ganz zu")),
                Question("Bedingungen meines Lebens sind ausgezeichnet", listOf("Stimme überhaupt nicht zu", "Stimme nicht zu", "Stimme eher nicht zu", "Weder noch", "Stimme eher zu", "Stimme zu", "Stimme voll und ganz zu")),
            )),
            Questionnaire("Selbstwirksamkeit", listOf(
                Question("Ich bin zuversichtlich, dass ich meine Ziele erreichen kann", listOf("Stimmt überhaupt nicht", "Stimmt nicht", "Stimmt eher nicht", "Weder noch", "Stimmt eher", "Stimmt", "Stimmt voll und ganz")),
                Question("Ich kann auch mit unerwarteten Ereignissen gut umgehen", listOf("Stimmt überhaupt nicht", "Stimmt nicht", "Stimmt eher nicht", "Weder noch", "Stimmt eher", "Stimmt", "Stimmt voll und ganz")),
                Question("Ich kann die meisten Probleme lösen, wenn ich mich bemühe", listOf("Stimmt überhaupt nicht", "Stimmt nicht", "Stimmt eher nicht", "Weder noch", "Stimmt eher", "Stimmt", "Stimmt voll und ganz")),
            ))
        )
    }
    val questionnaire = remember(type) { questionnaires.first { it.type == type } }
    val answers = remember { mutableStateListOf<Int>() }
    questionnaire.questions.forEach { _ -> answers.add(0) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(questionnaire.type, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(questionnaire.questions) { index, question ->
                QuestionItem(question, answers[index]) { answers[index] = it }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            val score = answers.sum()
            vm.addEvaluation(score, questionnaire.type)
            onEvaluationComplete()
        }) {
            Text("Abschließen")
        }
    }
}

@Composable
private fun QuestionItem(question: Question, selectedAnswer: Int, onAnswerSelected: (Int) -> Unit) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(question.text, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        question.options.forEachIndexed { index, option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = index == selectedAnswer,
                    onClick = { onAnswerSelected(index) }
                )
                Text(option)
            }
        }
    }
}
