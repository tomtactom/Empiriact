package com.empiriact.app.data

import android.content.Context

object AppGraph {
    lateinit var settings: SettingsRepository

    fun provide(context: Context) {
        settings = SettingsRepository(context)
    }
}
