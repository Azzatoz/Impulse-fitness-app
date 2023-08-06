package com.example.mydiplom_try2.additional_files

import android.os.SystemClock
import android.widget.Chronometer

class Timer(private val chronometer: Chronometer) {

    private var isRunning: Boolean = false

    fun startChronometer() {
        if (!isRunning) {
            chronometer.base = SystemClock.elapsedRealtime() // Сбрасываем базовое время хронометра на текущее время
            chronometer.start()
            isRunning = true
        }
    }

    fun stopChronometer() {
        chronometer.stop()
        isRunning = false
    }

    fun isRunning(): Boolean {
        return isRunning
    }
}
