package com.empiriact.app.ui.screens.leo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class LeoPage(
    val title: String,
    val text: String,
    val icon: ImageVector
)

private val leoPages = listOf(
    LeoPage(
        title = "Herzlich willkommen!",
        icon = Icons.Filled.Psychology,
        text = """Schön, dass du hier bist. Mein Name ist Leo und ich bin hier, um dich auf deinem Weg mit „Empiract“ zu unterstützen. Der Name „Empiract“ verbindet „Empirie“ und „Act“ – es geht also darum, durch eigenes Handeln wertvolle Erfahrungen zu sammeln. Du kannst durch die folgenden Seiten wischen, um mehr darüber zu erfahren, wie diese App dich unterstützen kann. Vielleicht beginnst du diesen Weg mit Neugierde, vielleicht auch mit Skepsis. Was auch immer du gerade empfindest, es hat hier seinen Platz. Ich bin ein digitales Programm, entwickelt, um dir als Werkzeug zu dienen und dich auf deiner Entdeckungsreise zu unterstützen."""
    ),
    LeoPage(
        title = "Du als Forscher*in",
        icon = Icons.Filled.Psychology,
        text = """In der nächsten Zeit kannst du erkunden, wie du deinen Alltag so gestalten kannst, dass er sich mehr nach deinen Werten und Bedürfnissen anfühlt – auch wenn gerade nicht alles einfach ist. Du bist eingeladen, dich selbst als Forscher*in deines Lebens zu sehen: Gemeinsam können wir schauen, was wirkt, was dir hilft und was eher nicht. Es gibt hier kein „richtig“ oder „falsch“ – sondern nur Erfahrungen, die du für dich sammeln kannst."""
    ),
    LeoPage(
        title = "Werteorientierte Aktivierung",
        icon = Icons.Filled.Route,
        text = """Der Ansatz, der hier vorgestellt wird, nennt sich „werteorientierte Verhaltensaktivierung“. Das klingt vielleicht theoretisch, ist aber ganz praktisch: Er bietet dir die Möglichkeit, selbst auszuprobieren, welche Aktivitäten deine Stimmung beeinflussen und wie du Aktivitäten finden kannst, die sich für dich stimmig anfühlen und mit dem im Einklang stehen, was dir wichtig ist."""
    ),
    LeoPage(
        title = "Du bist Expert*in",
        icon = Icons.Filled.Route,
        text = """Du bist dabei Expert:in, und ich unterstütze dich. Es gibt Übungen, die du zwischen unseren Treffen ausprobieren kannst. Und falls etwas nicht klappt, ist das völlig in Ordnung. Jede Erfahrung – auch die, die herausfordernd ist – hilft uns, dich und deine Bedürfnisse besser zu verstehen."""
    ),
    LeoPage(
        title = "Beobachten als erster Schritt",
        icon = Icons.Filled.Search,
        text = """Um eine Veränderung anzustoßen, kann es hilfreich sein, zunächst zu beobachten: Was machst du über den Tag verteilt und wie fühlst du dich bei verschiedenen Aktivitäten? Diese Beobachtungen, falls du sie teilen möchtest, können wertvolle Einblicke für den weiteren Weg liefern."""
    ),
    LeoPage(
        title = "Ehrlichkeit dir selbst gegenüber",
        icon = Icons.Filled.Search,
        text = """Dabei brauchst du dir keine Sorgen um Details zu machen, die Hauptaktivität der letzten Stunde ist oft schon ausreichend. Je ehrlicher du dabei mit dir selbst bist, desto hilfreichere Erkenntnisse kannst du für dich gewinnen – auch wenn mal „nichts“ passiert oder du Zeit mit etwas verbringst, das sich vielleicht „sinnlos“ anfühlt. All das ist Teil deiner Realität und kann wertvolle Hinweise enthalten."""
    ),
    LeoPage(
        title = "Sinn statt „positiv“",
        icon = Icons.Filled.Diamond,
        text = """Der Fokus liegt hier nicht darauf, möglichst viele „positive“ Aktivitäten zu sammeln, sondern solche zu finden, die für dich persönlich bedeutsam sind. Eine Orientierung dafür können deine persönlichen Werte sein – also das, was dir im Leben wirklich wichtig ist."""
    ),
    LeoPage(
        title = "Werte als Kompass",
        icon = Icons.Filled.Diamond,
        text = """Werte sind wie ein innerer Kompass, eine Orientierung, die dir Kraft geben kann. Gemeinsam können wir erkunden, was dir wichtig ist, wie es sich vielleicht im Alltag schon zeigt und wie es wieder mehr Raum in deinem Leben bekommen kann."""
    ),
    LeoPage(
        title = "Deine zentrale Rolle",
        icon = Icons.Filled.Flag,
        text = """Vielleicht ist jetzt schon spürbar: Du nimmst einen zentralen Teil hier ein. Du bist Handelnder – und wir lernen gemeinsam. Es geht um Ausprobieren, Beobachten und Erleben."""
    ),
    LeoPage(
        title = "Einladung zum Entdecken",
        icon = Icons.Filled.Flag,
        text = """Für die nächsten Wochen lade ich dich ein herauszufinden: Welche Erfahrungen möchtest du sammeln? Wo magst du neugierig hinschauen? Und wie kannst du deinen Alltag Schritt für Schritt wieder nach deinen eigenen Vorstellungen gestalten?"""
    ),
    LeoPage(
        title = "Deine Gedanken zum Start",
        icon = Icons.Filled.Flag,
        text = """Wenn du magst, schreib gleich mal auf, was dir beim Lesen durch den Kopf geht. Was klingt hilfreich? Was löst Widerstand aus? Das alles kann hier sein – und deine Antwort kann ein wertvoller Ausgangspunkt für unser gemeinsames Weitergehen sein."""
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeoScreen() {
    val pagerState = rememberPagerState { leoPages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            LeoPageContent(page = leoPages[pageIndex], isLastPage = pageIndex == leoPages.lastIndex)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Text("Zurück")
            }
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(leoPages.lastIndex))
                    }
                },
                enabled = pagerState.currentPage < leoPages.lastIndex
            ) {
                Text("Weiter")
            }
        }
    }
}

@Composable
private fun LeoPageContent(page: LeoPage, isLastPage: Boolean) {
    var text by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = page.icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (isLastPage) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Deine Gedanken") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
