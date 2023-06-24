package com.example.mydiplom_try2.makingYourOwnTraining

import android.app.Dialog
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.tabs.CreateWorkout
import com.example.mydiplom_try2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRecordOfTraining : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var magnetometer: Sensor
    private lateinit var gameRotationVector: Sensor

    private var accelerometerValues = FloatArray(3)
    private var gyroscopeValues = FloatArray(3)
    private var magnetometerValues = FloatArray(3)
    private var gameRotationVectorValues = FloatArray(3)

    private var isRunning: Boolean = false
    private lateinit var chronometer: Chronometer
    private lateinit var finishButton: Button
    private lateinit var countDownTextView: TextView

    private lateinit var tableName: String
    private lateinit var trainingDao: TrainingDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_of_training)
        supportActionBar?.hide() // скрыть заголовок

        // получаем имя таблицы/бд
        tableName = intent.getStringExtra("tableName") ?: ""

        chronometer = findViewById(R.id.chronometer)
        finishButton = findViewById(R.id.finishButton)
        countDownTextView = findViewById(R.id.countdownTextView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        gameRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        trainingDao = TrainingRoomDatabase.getInMemoryDatabase(this).trainingDao()

        // Start countdown before starting the chronometer
        Handler(Looper.getMainLooper()).postDelayed( {
            startChronometer()
            countDownTextView.text = ""
            registerSensors()
        }, 500)

        finishButton.setOnClickListener {
            if (isRunning) {
                stopChronometer() // стоп секундомер
                unregisterSensors() // стоп датчики
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_save_or_not)

        val buttonSave = dialog.findViewById<Button>(R.id.button_save)
        val buttonDiscard = dialog.findViewById<Button>(R.id.button_discard)
        dialog.show()

        buttonSave.setOnClickListener {
            saveRecord()
            dialog.dismiss() // Закрыть диалоговое окно
        }

        buttonDiscard.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@MainRecordOfTraining, CreateWorkout::class.java)
            startActivity(intent)

        }

        dialog.setOnDismissListener {
            unregisterSensors() // Вызов unregisterSensors() при закрытии диалогового окна
        }
    }

    private fun saveRecord() {
        val trainingRecord = TrainingRecord(
            tableName = tableName,
            date = System.currentTimeMillis(),
            duration = (SystemClock.elapsedRealtime() - chronometer.base) / 1000,
            accelerometerData = accelerometerValues.toList(),
            gyroscopeData = gyroscopeValues.toList(),
            magnetometerData = magnetometerValues.toList(),
        )

        CoroutineScope(Dispatchers.IO).launch {
            trainingDao.insert(trainingRecord)
            runOnUiThread {
                Toast.makeText(
                    this@MainRecordOfTraining,
                    "Тренировка сохранена",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val intent = Intent(this@MainRecordOfTraining, CreateWorkout::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        registerSensors()
    }

    override fun onPause() {
        super.onPause()
        unregisterSensors()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerValues = event.values.clone()
            Sensor.TYPE_GYROSCOPE -> gyroscopeValues = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerValues = event.values.clone()
            Sensor.TYPE_GAME_ROTATION_VECTOR -> gameRotationVectorValues = event.values.clone()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun registerSensors() {
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            this,
            gyroscope,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            this,
            magnetometer,
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            this,
            gameRotationVector,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    private fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    private fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
        isRunning = true
    }

    private fun stopChronometer() {
        chronometer.stop()
        isRunning = false
    }
}
