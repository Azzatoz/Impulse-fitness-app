package com.example.mydiplom_try2.additional_files

import android.hardware.SensorEventListener
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.TextView

class Timer(
    private val chronometer: Chronometer,
    private val countDownTextView: TextView,
    private val sensorManagerHelper: SensorManagerHelper,
    private val sensorEventListener: SensorEventListener
) {
    private var isRunning: Boolean = false

    fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
        isRunning = true
    }

    fun stopChronometer() {
        chronometer.stop()
        isRunning = false
    }

    /**
     * Запускает обратный отсчет перед началом тренировки.
     * После задержки отображает хронометр, скрывает текст с обратным отсчетом
     * и регистрирует слушателя датчика с помощью SensorManagerHelper.
     */
    fun startCountdown() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            chronometer.visibility = View.VISIBLE
            countDownTextView.visibility = View.INVISIBLE
            sensorManagerHelper.registerSensorEventListener(sensorEventListener)
        }, 500)
    }


    fun isRunning(): Boolean {
        return isRunning
    }
}
