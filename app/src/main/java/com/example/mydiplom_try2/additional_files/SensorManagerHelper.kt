package com.example.mydiplom_try2.additional_files

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorManagerHelper(private val context: Context) {
    private lateinit var sensorManager: SensorManager
    private lateinit var gameRotationVector: Sensor

    fun getGameRotationVectorSensor(): Sensor {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
    }

    fun registerSensorEventListener(sensorEventListener: SensorEventListener) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gameRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        sensorManager.registerListener(
            sensorEventListener,
            gameRotationVector,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun unregisterSensorEventListener(sensorEventListener: SensorEventListener) {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
