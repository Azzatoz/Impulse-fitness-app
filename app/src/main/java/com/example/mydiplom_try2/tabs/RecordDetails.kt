package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_record_details)

        // Получаем данные о тренировке из интента
        val recordName = intent.getStringExtra(KEY_RECORD_NAME)!!

        // Выполняем запрос к базе данных для получения записи по имени тренировки
        lifecycleScope.launch {
            val recordMeta = withContext(Dispatchers.IO) {
                MyRoomDatabase.getDatabase(this@RecordDetails, lifecycleScope, "").metaDao()
                    .getRecordByName(recordName)
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
            }
        }
    }
    private fun formatDuration(durationInSeconds: Long): String {
        val hours = durationInSeconds / 3600
        val minutes = (durationInSeconds % 3600) / 60
        val seconds = durationInSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    companion object {
        private const val KEY_RECORD_NAME = "recordName"
    }
}
