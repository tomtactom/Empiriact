package com.empiriact.app.ui.screens.overview

import com.empiriact.app.R
import org.junit.Assert.assertEquals
import org.junit.Test

class OverviewProtocolStepFormattingTest {

    @Test
    fun `maps disabled status to tracking off text resource`() {
        assertEquals(
            R.string.protocol_steps_tracking_disabled,
            protocolStepTextRes(StepDisplayState.Disabled)
        )
    }

    @Test
    fun `maps permission missing reason to permission text resource`() {
        assertEquals(
            R.string.protocol_steps_permission_missing,
            protocolStepTextRes(
                StepDisplayState.NotRecorded(StepDisplayState.NotRecorded.Reason.PERMISSION_MISSING)
            )
        )
    }

    @Test
    fun `maps unknown not recorded reason to generic text resource`() {
        assertEquals(
            R.string.protocol_steps_not_recorded,
            protocolStepTextRes(StepDisplayState.NotRecorded())
        )
    }
}
