package com.empiriact.app.notifications

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

class HourlyPromptSchedulerTest {

    @Test
    fun `returns full delay when before target minute`() {
        val now = ZonedDateTime.of(2026, 1, 1, 10, 40, 0, 0, ZoneId.of("UTC"))

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now)

        assertEquals(Duration.ofMinutes(15), delay)
    }

    @Test
    fun `enforces minimum delay at exact boundary`() {
        val now = ZonedDateTime.of(2026, 1, 1, 10, 55, 0, 0, ZoneId.of("UTC"))

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now)

        assertEquals(Duration.ofHours(1), delay)
    }

    @Test
    fun `rolls over after target minute`() {
        val now = ZonedDateTime.of(2026, 1, 1, 23, 59, 58, 0, ZoneId.of("UTC"))

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now)

        assertEquals(Duration.ofMinutes(55).plusSeconds(2), delay)
    }

    @Test
    fun `minimum delay is five seconds when now is within same second before target`() {
        val now = ZonedDateTime.of(2026, 1, 1, 10, 54, 59, 0, ZoneId.of("UTC"))

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now)

        assertEquals(Duration.ofSeconds(5), delay)
    }

    @Test
    fun `handles spring-forward gap with zone-aware delay`() {
        val now = ZonedDateTime.of(2026, 3, 29, 1, 55, 0, 0, ZoneId.of("Europe/Berlin"))

        val delay = HourlyPromptScheduler.computeDelayUntilNextPrompt(now)

        assertEquals(Duration.ofMinutes(60), delay)
    }
}
