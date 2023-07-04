package com.example.mydiplom_try2.makingYourOwnTraining

import android.app.Dialog
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SensorManagerHelper
import com.example.mydiplom_try2.additional_files.TrainingTimer
import com.example.mydiplom_try2.tabs.CreateWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRecordOfTraining : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManagerHelper: SensorManagerHelper
    private lateinit var gameRotationVector: Sensor
    private var gameRotationVectorValues = FloatArray(3)
    private val trainingRecords: MutableList<TrainingRecord> = mutableListOf()
    private lateinit var chronometer: Chronometer
    private lateinit var finishButton: Button
    private lateinit var countDownTextView: TextView
    private lateinit var tableName: String
    private lateinit var description: String
    private lateinit var trainingDao: TrainingDao
    private lateinit var trainingTimer: TrainingTimer
    private lateinit var database: TrainingRoomDatabase // Добавляем переменную для базы данных

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_of_training)
        supportActionBar?.hide() // Скрыть заголовок

        // Получаем имя таблицы/базы данных
        tableName = intent.getStringExtra("tableName") ?: ""
        description = intent.getStringExtra("description") ?: ""
        chronometer = findViewById(R.id.chronometer)
        finishButton = findViewById(R.id.finishButton)
        countDownTextView = findViewById(R.id.countdownTextView)
        sensorManagerHelper = SensorManagerHelper(this)
        gameRotationVector = sensorManagerHelper.getGameRotationVectorSensor()

        // Инициализируем базу данных
        database = TrainingRoomDatabase.getDatabase(this, CoroutineScope(Dispatchers.IO))

        trainingDao = database.trainingDao() // Используем DAO из базы данных
        trainingTimer = TrainingTimer(chronometer, countDownTextView, sensorManagerHelper, this)
        // Начинаем обратный отсчет перед запуском хронометра
        Handler(Looper.getMainLooper()).postDelayed({
            trainingTimer.startChronometer()
            countDownTextView.text = ""
            trainingTimer.startCountdown()
        }, 500)
        finishButton.setOnClickListener {
            if (trainingTimer.isRunning()) {
                trainingTimer.stopChronometer() // Останавливаем секундомер
                unregisterSensors() // Отключаем датчики
                showDialog()
            }
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

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            gameRotationVectorValues = event.values.clone()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun registerSensors() {
        sensorManagerHelper.registerSensorEventListener(this)
    }

    private fun unregisterSensors() {
        sensorManagerHelper.unregisterSensorEventListener(this)
    }

    // Отображение диалогового окна
    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_save_or_not)
        val buttonSave = dialog.findViewById<Button>(R.id.button_save)
        val buttonDiscard = dialog.findViewById<Button>(R.id.button_discard)
        dialog.show()

        buttonSave.setOnClickListener {
            saveTrainingRecordsToDatabase()
            dialog.dismiss() // Закрыть диалоговое окно
            val intent = Intent(this@MainRecordOfTraining, CreateWorkout::class.java)
            startActivity(intent)
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

    // Сохранение записи тренировки
    private fun saveTrainingRecordsToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            trainingRecords.forEach { trainingRecord ->
                trainingDao.insert(trainingRecord)
            }
            runOnUiThread {
                Toast.makeText(
                    this@MainRecordOfTraining,
                    "Сохранение успешно",
                    Toast.LENGTH_SHORT
                ).show()
            }
            trainingRecords.clear() // Очищаем список записей тренировки после сохранения в базу данных
        }
    }
}
