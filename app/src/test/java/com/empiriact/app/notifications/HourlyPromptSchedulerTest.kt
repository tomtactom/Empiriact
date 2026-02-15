package com.empiriact.app.notifications

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

class HourlyPromptSchedulerTest {

    @Test
    fun `returns full delay when before target minute`() {
        val now = LocalDateTime.of(2026, 1, 1, 10, 40, 0)

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now, ZoneId.of("UTC"))

        assertEquals(Duration.ofMinutes(15), delay)
    }

    @Test
    fun `enforces minimum delay at exact boundary`() {
        val now = LocalDateTime.of(2026, 1, 1, 10, 55, 0)

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now, ZoneId.of("UTC"))

        assertEquals(Duration.ofHours(1), delay)
    }

    @Test
    fun `rolls over after target minute`() {
        val now = LocalDateTime.of(2026, 1, 1, 23, 59, 58)

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now, ZoneId.of("UTC"))

        assertEquals(Duration.ofMinutes(55).plusSeconds(2), delay)
    }

    @Test
    fun `minimum delay is five seconds when now is within same second before target`() {
        val now = LocalDateTime.of(2026, 1, 1, 10, 54, 59)

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now, ZoneId.of("UTC"))

        assertEquals(Duration.ofSeconds(5), delay)
    }
}
