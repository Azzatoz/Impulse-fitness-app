package com.example.mydiplom_try2.tabs

import android.content.Intent
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.DatabaseHelper.Companion.COLUMN_DATE
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class StatisticsOfSquats : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var soundManager: SoundManager // объявляем переменную soundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // скрыть заголовок
        setContentView(R.layout.activity_statistics_of_squats)

        databaseHelper = DatabaseHelper(this)

        // Получаем данные из БД и отображаем их в таблице
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val cursor = databaseHelper.getAllStatistics()
        val cancelButton = findViewById<ImageButton>(R.id.back_button)
        soundManager = SoundManager
        SoundManager.init(this)

        cancelButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(
                this, Profile::class.java) // перенаправляет нажатием на MainMenu
            startActivity(intent)
        }



        if (cursor.moveToFirst()) {
            do {
                val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
                val timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME)
                val countIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_COUNT)

                // Проверяем наличие столбцов
                if (dateIndex == -1 || timeIndex == -1 || countIndex == -1) {
                    continue
                }

                val date = cursor.getString(dateIndex)
                val time = cursor.getString(timeIndex)
                val count = cursor.getInt(countIndex)

                val row = TableRow(this)
                val dateTextView = TextView(this)
                dateTextView.text = date
                dateTextView.setPadding(10, 10, 10, 10)
                row.addView(dateTextView)

                val timeTextView = TextView(this)
                timeTextView.text = time
                timeTextView.setPadding(10, 10, 10, 10)
                row.addView(timeTextView)

                val countTextView = TextView(this)
                countTextView.text = count.toString()
                countTextView.setPadding(10, 10, 10, 10)
                row.addView(countTextView)

                tableLayout.addView(row)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}
