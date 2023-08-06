package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import com.example.mydiplom_try2.useRecords.Playback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordDetails : AppCompatActivity() {

    private lateinit var database: MyRoomDatabase
    private lateinit var recordName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_record_details)

        // Получаем данные о тренировке из интента
        recordName = intent.getStringExtra("recordName") ?: ""

        // Инициализируем базу данных
        database = MyRoomDatabase.getDatabase(this@RecordDetails, lifecycleScope, recordName)
        // Выполняем запрос к базе данных для получения записи по имени тренировки
        lifecycleScope.launch {
            val recordMeta = withContext(Dispatchers.IO) {
                database.metaDao().getRecordByName(recordName)
            }

            if (recordMeta != null) {
                // Получаем длительность и описание из записи
                val recordDuration = recordMeta.duration
                val recordDescription = recordMeta.description

                // Устанавливаем значения полей TextView в соответствии с данными о тренировке
                findViewById<TextView>(R.id.record_name_text_view).text = recordName
                findViewById<TextView>(R.id.record_duration_text_view).text = formatDuration(recordDuration)

                if (recordDescription.isNullOrBlank()) {
                    findViewById<TextView>(R.id.record_description_text_view).text = getString(R.string.default_description)
                } else {
                    findViewById<TextView>(R.id.record_description_text_view).text = recordDescription
                }

                // Находим кнопку start_button и устанавливаем на нее слушатель кликов
                val startButton = findViewById<Button>(R.id.start_button)
                startButton.setOnClickListener {
                    val intent = Intent(this@RecordDetails, Playback::class.java)
                    intent.putExtra("recordDuration", recordDuration)
                    intent.putExtra("recordName", recordName)
                    startActivity(intent)
                }

            }
        }
    }
    private fun formatDuration(durationInSeconds: Long): String {
        val hours = durationInSeconds / 3600
        val minutes = (durationInSeconds % 3600) / 60
        val seconds = durationInSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
