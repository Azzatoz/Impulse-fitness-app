package com.example.mydiplom_try2.tabs  //главное меню

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.HeightDatabase
import com.example.mydiplom_try2.additional_files.HeightDatabase.Companion.TABLE_HEIGHT
import com.example.mydiplom_try2.additional_files.SoundManager

class MainActivity : AppCompatActivity() {

    private lateinit var soundManager: SoundManager // объявляем переменную soundManager
    private lateinit var heightDH: HeightDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        supportActionBar?.hide() // скрыть заголовок
        // Убрать нижнюю полоску навигационной панели
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        heightDH = HeightDatabase(this)
        val db = heightDH.readableDatabase

        // Проверка на рост в БД
        if (DatabaseUtils.queryNumEntries(db, TABLE_HEIGHT) == 0L) {
            showInputDialog(this)
        }

        // Объявление кнопок
        val startButton = findViewById<Button>(R.id.start_button)
        val statisticsButton = findViewById<Button>(R.id.statistics_button)
        val soundButton = findViewById<ImageButton>(R.id.sound_button)
        val settingsButton = findViewById<ImageButton>(R.id.settings_button)

        soundManager = SoundManager
        SoundManager.init(this)

        soundButton.setOnClickListener {
            soundManager.toggleSound() // включаем/отключаем звук с помощью SoundManager
            updateSoundButton() // обновляем картинку кнопки
        }

        settingsButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, ExerciseSelectionMenu::class.java)
            startActivity(intent)
        }

        statisticsButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, MainStatistics::class.java)
            startActivity(intent)
        }

    }

    private fun updateSoundButton() {
        // TODO: Обновляем картинку кнопки в зависимости от состояния звука
        val imageResId = if (soundManager.isSoundOn()) R.drawable.sound_icon_on else R.drawable.sound_icon_off
        findViewById<ImageButton>(R.id.sound_button).setImageResource(imageResId)
    }

    private fun showInputDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.height_input)
        dialog.setCancelable(false)
        val heightDB = HeightDatabase(context)
        val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker_cm)
        numberPicker.minValue = 130
        numberPicker.maxValue = 250
        val millimetersPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker_mm)
        millimetersPicker.minValue = 0
        millimetersPicker.maxValue = 9
        dialog.setTitle("Выберите ваш рост")
        dialog.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            val height = "${numberPicker.value}.${millimetersPicker.value}".toFloat()
            heightDB.addHeight(height)
            dialog.dismiss()
        }
        dialog.show()
    }
}
