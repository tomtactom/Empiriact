package com.empiriact.app.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CheckinRepository {

    private val questionnairesList = listOf(
        Questionnaire(
            id = "well_being",
            title = "Wohlbefinden",
            description = "Schätze dein allgemeines Wohlbefinden in den letzten Tagen ein.",
            state = QuestionnaireState.ON,
            icon = Icons.Default.SentimentSatisfiedAlt
        ),
        Questionnaire(
            id = "cbt_basics",
            title = "CBT Grundlagen",
            description = "Ein kurzer Fragebogen, um dein Wissen über die Grundlagen der kognitiven Verhaltenstherapie zu testen.",
            state = QuestionnaireState.ON,
            icon = Icons.Default.Psychology
        ),
        Questionnaire(
            id = "coping_strategies",
            title = "Umgang mit Stress",
            description = "Bewerte, wie gut du in letzter Zeit mit Stress umgegangen bist.",
            state = QuestionnaireState.ON,
            icon = Icons.Default.CrisisAlert
        ),
        Questionnaire(
            id = "infinity_scroll",
            title = "Endloses Scrollen",
            description = "Dieser Fragebogen ist ein Beispiel für einen endlosen Scroll-Test.",
            state = QuestionnaireState.PAUSED,
            icon = Icons.Default.AllInclusive
        ),
        Questionnaire(
            id = "data_analysis",
            title = "Datenanalyse",
            description = "Dieser Fragebogen ist derzeit in Entwicklung und noch nicht verfügbar.",
            state = QuestionnaireState.OFF,
            icon = Icons.Default.Analytics
        )
    )

    fun getQuestionnaires(): Flow<List<Questionnaire>> {
        return flowOf(questionnairesList)
    }

    fun getWellbeingQuestionnaire(): WellbeingQuestionnaire {
        return WellbeingQuestionnaire(
            id = "well_being",
            title = "Wohlbefinden",
            description = "Schätze dein allgemeines Wohlbefinden in den letzten Tagen ein.",
            icon = Icons.Default.SentimentSatisfiedAlt,
            instruction = "Die folgenden Fragen beziehen sich auf deine Gedanken und Gefühle in den letzten zwei Wochen. Bitte gib an, wie oft du dich so gefühlt hast.",
            items = listOf(
                QuestionnaireItem(id = "q1", text = "Ich war zuversichtlich und optimistisch, was die Zukunft angeht."),
                QuestionnaireItem(id = "q2", text = "Ich habe mich nützlich und gebraucht gefühlt."),
                QuestionnaireItem(id = "q3", text = "Ich konnte mich entspannen und zur Ruhe kommen."),
                QuestionnaireItem(id = "q4", text = "Ich hatte Energie und Tatendrang."),
                QuestionnaireItem(id = "q5", text = "Ich konnte Probleme gut bewältigen.")
            ),
            scale = LikertScale(
                lowAnchor = "Gar nicht",
                highAnchor = "Die ganze Zeit"
            ),
            interpretations = listOf(
                ScoreInterpretation(
                    scoreRange = 5..10,
                    title = "Niedriges Wohlbefinden",
                    interpretation = "Deine Antworten deuten darauf hin, dass dein seelisches Wohlbefinden aktuell stark beeinträchtigt sein könnte. Es scheint, als ob du nur selten positive Gefühle wie Optimismus oder Entspannung erlebst.",
                    recommendation = "Es könnte hilfreich sein, eine kleine, angenehme Aktivität in deinen Tag einzuplanen. Vielleicht ein kurzer Spaziergang oder ein Telefonat mit einem Freund?"
                ),
                ScoreInterpretation(
                    scoreRange = 11..18,
                    title = "Moderates Wohlbefinden",
                    interpretation = "Du erlebst eine Mischung aus guten und schlechten Tagen. Es gibt Momente, in denen du dich energiegeladen und zuversichtlich fühlst, aber auch Phasen, in denen die Anspannung überwiegt.",
                    recommendation = "Achte darauf, was dir an den guten Tagen hilft. Vielleicht kannst du diese Aktivitäten gezielt einsetzen, wenn du dich niedergeschlagen fühlst."
                ),
                ScoreInterpretation(
                    scoreRange = 19..25,
                    title = "Hohes Wohlbefinden",
                    interpretation = "Deine Antworten deuten auf ein hohes seelisches Wohlbefinden hin. Du scheinst dich die meiste Zeit über energiegeladen, optimistisch und entspannt zu fühlen.",
                    recommendation = "Super! Nutze diese positive Energie, um deine Werte und Ziele weiter zu verfolgen. Was möchtest du als Nächstes erreichen?"
                )
            )
        )
    }

    fun getQuestionnaireById(id: String): Questionnaire? {
        return questionnairesList.find { it.id == id }
    }
}
