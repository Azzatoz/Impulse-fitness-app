package com.example.mydiplom_try2.tabs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class ExerciseStartSquatsActivity : AppCompatActivity() {

    private lateinit var soundManager: SoundManager // объявляем переменную soundManager
    private var minutes: Int = 2
    private var seconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_start_squats)
        supportActionBar?.hide() // скрыть заголовок

        val cancelButton = findViewById<ImageButton>(R.id.imageButton_cancel1) // кнопка выхода из exercise_start1
        val timerTextView = findViewById<TextView>(R.id.timerTextView1) // таймер
        val startButton = findViewById<Button>(R.id.startButton1)
        soundManager = SoundManager


        fun showTimePickerDialog() {
            soundManager.playSound()
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.timer)
            val minutesPicker = dialog.findViewById<NumberPicker>(R.id.np_minutes)
            val secondsPicker = dialog.findViewById<NumberPicker>(R.id.np_seconds)
            val okButton = dialog.findViewById<Button>(R.id.btn_ok)

            minutesPicker.minValue = 0
            minutesPicker.maxValue = 59

            secondsPicker.minValue = 0
            secondsPicker.maxValue = 59

            dialog.setCancelable(true)
            okButton.setOnClickListener {
                minutes = minutesPicker.value
                seconds = secondsPicker.value
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
                dialog.dismiss()
            }
            dialog.show()
        }

        startButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, ActiveExerciseSquats::class.java) // начать упражнение
            intent.putExtra("minutes", minutes)
            intent.putExtra("seconds", seconds)
            startActivity(intent)
        }


        cancelButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(
                this, ExerciseSelectionMenu::class.java
            ) // перенаправляет нажатием на exercise_selection_menu
            startActivity(intent)
        }

        timerTextView.setOnClickListener {
            soundManager.playSound()
            showTimePickerDialog()
        }
    }
}
