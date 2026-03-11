package com.empiriact.app.ui.screens.insights

import org.junit.Assert.assertEquals
import org.junit.Test

class InsightsScreenFormattingTest {

    @Test
    fun `adds quality tag for estimated passive value`() {
        assertEquals(
            "• Schrittzahl: 120 Schritte (geschätzt)",
            formatPassiveReadingLine("Schrittzahl", "120 Schritte", isEstimated = true)
        )
    }

    @Test
    fun `does not add quality tag for precise passive value`() {
        assertEquals(
            "• Schrittzahl: 120 Schritte",
            formatPassiveReadingLine("Schrittzahl", "120 Schritte", isEstimated = false)
        )
    }
}
