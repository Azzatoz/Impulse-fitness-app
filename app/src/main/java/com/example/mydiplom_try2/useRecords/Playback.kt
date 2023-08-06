package com.example.mydiplom_try2.useRecords

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.SensorManagerHelper
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class Playback : AppCompatActivity(), SensorEventListener {

    // UI elements
    private lateinit var timerTextView: Chronometer
    private lateinit var recordNameTextView: TextView
    private lateinit var pauseButton: Button
    private lateinit var correctTextView: TextView
    private lateinit var almostCorrectTextView: TextView
    private lateinit var incorrectTextView: TextView

    // Other properties
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var sensorManagerHelper: SensorManagerHelper
    private lateinit var database: MyRoomDatabase

    private var correctCount: Int = 0
    private var almostCorrectCount: Int = 0
    private var incorrectCount: Int = 0
    private var score: Int = 0
    private var accuracy: Double = 0.0
    private var isTimerRunning = false
    private var recordId: Int = 0
    private var isMatching: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        // Get data from Intent
        val recordDuration = intent.getLongExtra("recordDuration", 2000)
        val recordName = intent.getStringExtra("recordName") ?: ""

        // Initialize the Room database with the name "training_db_$recordName"
        database = MyRoomDatabase.getDatabase(this, lifecycleScope, recordName)

        // Initialize UI elements
        timerTextView = findViewById(R.id.timer)
        recordNameTextView = findViewById(R.id.record_name_text_view)
        correctTextView = findViewById(R.id.correct_text_view)
        almostCorrectTextView = findViewById(R.id.almostCorrect_text_view)
        incorrectTextView = findViewById(R.id.incorrect_text_view)
        pauseButton = findViewById(R.id.pause_button)

        // Устанавливаем текст для recordNameTextView
        recordNameTextView.text = recordName

        // Start the countdown timer with an interval of 1 second
        countDownTimer = object : CountDownTimer(recordDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Обновите текст в timerTextView с оставшимся временем
                timerTextView.text = formatDuration(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                // Вычисляем точность выполнения в процентах
                accuracy = calculateAccuracy()

                // Сохраняем данные в базу данных
                saveDataToDatabase(recordName, accuracy)

                // Устанавливаем состояние таймера как остановленный
                isTimerRunning = false
            }
        }
        countDownTimer.start()

        // Кнопка паузы
        pauseButton.setOnClickListener {
            // Здесь добавьте логику для паузы и возобновления таймера при нажатии на кнопку
            if (isTimerRunning) {
                countDownTimer.cancel()
            } else {
                countDownTimer.start()
            }
            // Изменяем состояние таймера после нажатия кнопки
            isTimerRunning = !isTimerRunning
        }

        // Initialize SensorManagerHelper
        sensorManagerHelper = SensorManagerHelper(this)

        // Регистрируем слушателя событий сенсора при создании и возобновлении активности
        registerSensors()
    }

    override fun onPause() {
        super.onPause()
        // Снимаем слушателя событий сенсора при приостановке активности
        unregisterSensors()
    }

    // Функция для регистрации слушателя событий сенсора
    private fun registerSensors() {
        // Используем анонимный объект вместо параметра sensorEventListener
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                    val sensorValues = event.values.clone()
                    // Start a coroutine to process sensor data in the background
                    lifecycleScope.launch {
                        // Get records from the database in the background
                        val recordsFromDatabase = withContext(Dispatchers.IO) {
                            getRecordsFromDatabase()
                        }
                        if (recordsFromDatabase.isNotEmpty()) {
                            processGameRotationVectorData(sensorValues, recordsFromDatabase)
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Add logic to handle sensor accuracy changes, if necessary
            }
        }

        // Регистрируем слушателя событий сенсора с использованием анонимного объекта
        sensorManagerHelper.registerSensorEventListener(sensorEventListener)
    }


    // Функция для отключения слушателя событий сенсора
    private fun unregisterSensors() {
        sensorManagerHelper.unregisterSensorEventListener(this)
    }

    // Функция для обработки данных от сенсора GameRotationVector
    private fun processGameRotationVectorData(
        sensorValues: FloatArray,
        recordsFromDatabase: List<Pair<Int, FloatArray>>
    ) {
        // Реализуйте здесь логику для сравнения данных с сенсора и записей из базы данных
        // и увеличение соответствующих счетчиков
        // Предполагается, что у вас есть переменные correctCount, almostCorrectCount и incorrectCount,
        // которые вы должны увеличивать в зависимости от правильности сравнения данных

        // Пример логики:
        if (isMatching) {
            // Проверяем, насколько близко данные сенсора к записи в базе данных
            val threshold = 0.1
            var isAlmostCorrect = true
            val recordValues = recordsFromDatabase.find { it.first == recordId }?.second
            if (recordValues != null) {
                for (i in sensorValues.indices) {
                    val diff = abs(sensorValues[i] - recordValues[i])
                    if (diff >= threshold) {
                        isAlmostCorrect = false
                        break
                    }
                }
            }
            if (isAlmostCorrect) {
                almostCorrectCount++
                almostCorrectTextView.text = almostCorrectCount.toString()
            } else {
                correctCount++
                correctTextView.text = correctCount.toString()
            }
        } else {
            incorrectCount++
            incorrectTextView.text = incorrectCount.toString()
            showToastWithSensorData(sensorValues[0], sensorValues[1], sensorValues[2])
        }
    }

    // Функция для получения записей из базы данных
    private fun getRecordsFromDatabase(): List<Pair<Int, FloatArray>> {
        // Выполняем запрос к базе данных для получения записей с заданным идентификатором
        val recordEntityList = database.recordDao().getAllGameRotationVectorData()
        return recordEntityList.map { recordEntity ->
            recordEntity.id to recordEntity.gameRotationVectorData.toFloatArray()
        }
    }

    // Функция для сохранения данных в базу данных
    private fun saveDataToDatabase(recordName: String, accuracy: Double) {
        val currentTime = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(currentTime))
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentTime))

        // Используем существующий экземпляр DatabaseHelper вместо создания нового
        val databaseHelper = DatabaseHelper(this)
        databaseHelper.addStatistic(recordName, date, time, score, accuracy)
    }

    // Функция для форматирования времени в формате HH:mm:ss
    private fun formatDuration(durationInSeconds: Long): String {
        val hours = durationInSeconds / 3600
        val minutes = (durationInSeconds % 3600) / 60
        val seconds = durationInSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Функция для вывода данных из GameRotationVectorData в Toast
    private fun showToastWithSensorData(x: Float, y: Float, z: Float) {
        val message = "X: $x, Y: $y, Z: $z"
        Toast.makeText(this@Playback, message, Toast.LENGTH_SHORT).show()
    }

    // Функция для расчета точности выполнения


    override fun onSensorChanged(event: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }
}
