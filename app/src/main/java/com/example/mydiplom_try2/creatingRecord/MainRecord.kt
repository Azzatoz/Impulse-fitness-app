package com.example.mydiplom_try2.creatingRecord

import android.app.Dialog
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SensorManagerHelper
import com.example.mydiplom_try2.additional_files.Timer
import com.example.mydiplom_try2.tabs.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainRecord : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManagerHelper: SensorManagerHelper
    private lateinit var gameRotationVector: Sensor
    private var gameRotationVectorValues = FloatArray(3)
    private val recordEntities: MutableList<RecordEntity> = mutableListOf()
    private lateinit var chronometer: Chronometer
    private lateinit var finishButton: Button
    private lateinit var countDownTextView: TextView
    private lateinit var metaDao: MetaDao
    private lateinit var timer: Timer
    private lateinit var database: MyRoomDatabase
    private var databaseName: String = ""
    private var description: String = ""
    private var countdownStartTime: Long = 0 // Время начала обратного отсчета

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        supportActionBar?.hide() // Скрыть заголовок

        chronometer = findViewById(R.id.chronometer)
        finishButton = findViewById(R.id.finishButton)
        countDownTextView = findViewById(R.id.countdownTextView)
        sensorManagerHelper = SensorManagerHelper(this)
        gameRotationVector = sensorManagerHelper.getGameRotationVectorSensor()

        // Получение переданного имени таблицы из intent
        databaseName = intent.getStringExtra("databaseName") ?: ""
        description = intent.getStringExtra("description") ?: ""

        database = MyRoomDatabase.getDatabase(
            this,
            CoroutineScope(Dispatchers.IO),
            databaseName
        ) // Инициализируем базу данных
        metaDao = database.metaDao()

        timer = Timer(chronometer)
        chronometer.visibility = View.INVISIBLE // Скрываем секундомер

        // Начинаем обратный отсчет перед запуском сенсоров и секундомера
        val handler = Handler(Looper.getMainLooper())
        countdownStartTime = System.currentTimeMillis() // Сохраняем текущее время
        handler.postDelayed({
            countDownTextView.text = "3" // Отображаем "3" на экране
            handler.postDelayed(
                { countDownTextView.text = "2" },
                1000
            ) // Отображаем "2" после 1 секунды
            handler.postDelayed(
                { countDownTextView.text = "1" },
                2000
            ) // Отображаем "1" после еще 1 секунды
            handler.postDelayed({
                countDownTextView.text = "" // Убираем текст
                chronometer.visibility = View.VISIBLE // Показываем хронометр
                countdownStartTime = System.currentTimeMillis() // Сохраняем время окончания обратного отсчета
                timer.startChronometer() // Запускаем хронометр после обратного отсчета
                registerSensors() // Регистрируем датчики
            }, 3000) // После еще 1 секунды (итого 3 секунды) запускаем сенсоры и хронометр
        }, 500) // Ждем 0.5 секунды перед началом обратного отсчета



        finishButton.setOnClickListener {
            if (timer.isRunning()) {
                timer.stopChronometer() // Останавливаем секундомер
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
            // Создание объекта TrainingRecord и сохранение его в базу данных
            val recordEntity = RecordEntity(
                gameRotationVectorData = gameRotationVectorValues.toList()
            )
            recordEntities.add(recordEntity) // Добавление записи в список trainingRecords
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
            saveRecordsToDatabase()
            dialog.dismiss() // Закрыть диалоговое окно
            val intent = Intent(this@MainRecord, MenuActivity::class.java)
            startActivity(intent)
        }

        buttonDiscard.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@MainRecord, MenuActivity::class.java)
            startActivity(intent)
        }

        dialog.setOnDismissListener {
            unregisterSensors() // Вызов unregisterSensors() при закрытии диалогового окна
        }
    }

    // Сохранение записи тренировки
    private fun saveRecordsToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = database.recordDao()

            recordEntities.forEach { record ->
                val recordEntity = RecordEntity(
                    gameRotationVectorData = record.gameRotationVectorData
                )
                recordDao.insert(recordEntity)
            }

            val currentTime = System.currentTimeMillis()
            val totalDurationInSeconds = (currentTime - countdownStartTime) / 1000 // Вычисляем общую длительность секундомера с учетом обратного отсчета

            val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val formattedDate = sdf.format(currentTime)

            val meta = MetaEntity(
                name_of_record = databaseName,
                date = formattedDate,
                duration = totalDurationInSeconds,
                description = description
            )
            metaDao.insert(meta)

            val isSavingSuccessful = true // Проверка успешного сохранения

            runOnUiThread {
                if (isSavingSuccessful) {
                    Toast.makeText(
                        this@MainRecord,
                        "Сохранение успешно",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainRecord,
                        "Ошибка сохранения",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            recordEntities.clear()
        }
    }
}
