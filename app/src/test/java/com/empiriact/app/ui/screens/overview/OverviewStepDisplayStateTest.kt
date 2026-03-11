package com.empiriact.app.ui.screens.overview

import com.empiriact.app.data.SettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class OverviewStepDisplayStateTest {

    @Test
    fun `returns recorded when step count exists including zero`() {
        assertEquals(
            StepDisplayState.Recorded(0),
            resolveStepDisplayState(
                stepCount = 0,
                passiveStepsEnabled = true,
                lastReadErrorReason = null,
                baselineHourPending = false
            )
        )
    }

    @Test
    fun `returns disabled when tracking is turned off`() {
        assertEquals(
            StepDisplayState.Disabled,
            resolveStepDisplayState(
                stepCount = null,
                passiveStepsEnabled = false,
                lastReadErrorReason = null,
                baselineHourPending = false
            )
        )
    }

    @Test
    fun `returns permission missing when last read failed due to missing permission`() {
        assertEquals(
            StepDisplayState.NotRecorded(StepDisplayState.NotRecorded.Reason.PERMISSION_MISSING),
            resolveStepDisplayState(
                stepCount = null,
                passiveStepsEnabled = true,
                lastReadErrorReason = SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING,
                baselineHourPending = false
            )
        )
    }
}
