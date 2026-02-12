package com.empiriact.app.ui.common

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object SoundManager {
    private var soundPool: SoundPool? = null
    private var gongSoundId = -1
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        isInitialized = true
    }

    fun playGongSound(context: Context) {
        if (!isInitialized) {
            initialize(context)
        }

        // Generiere einen synthetischen Gong-Ton durch gepitchte Töne
        // Dies ist eine vereinfachte Lösung - in Produktion würde man eine Audio-Datei verwenden
        Thread {
            try {
                // Einfacher Gong-ähnlicher Ton mit Oszillator-Emulation
                val sampleRate = 44100
                val duration = 2000 // 2 Sekunden
                val samples = (sampleRate * duration / 1000).toInt()
                val audioData = ShortArray(samples)

                // Erzeuge einen klingenden Ton
                for (i in 0 until samples) {
                    val time = i.toDouble() / sampleRate
                    val freq1 = 196.0 * Math.sin(2 * Math.PI * 196.0 * time) // Tiefton
                    val freq2 = 294.0 * Math.sin(2 * Math.PI * 294.0 * time) // Mittelton
                    val freq3 = 392.0 * Math.sin(2 * Math.PI * 392.0 * time) // Hochton

                    // Abklingender Effekt
                    val decay = Math.exp(-time / 0.5)
                    val combined = (freq1 + freq2 + freq3) / 3.0 * decay * 5000
                    audioData[i] = combined.toInt().toShort()
                }

                // Hinweis: SoundPool benötigt ein von Android bereitgestelltes Sound-Dateisystem
                // Dies ist eine Vereinfachung für Demo-Zwecke
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
    }
}
