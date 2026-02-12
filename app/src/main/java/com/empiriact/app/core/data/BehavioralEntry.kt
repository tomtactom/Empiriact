package com.empiriact.app.data

import java.util.UUID

data class BehavioralEntry(
    val id: UUID = UUID.randomUUID(),
    val activity: String,
    val mood: Int, // -2 to +2
    val startTime: Long,
    val endTime: Long,
    val valueName: String? = null
)
