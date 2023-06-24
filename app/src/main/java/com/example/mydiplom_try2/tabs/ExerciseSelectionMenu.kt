package com.example.mydiplom_try2.tabs  // выбор упражнения

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class ExerciseSelectionMenu : AppCompatActivity() {

    private lateinit var soundManager: SoundManager // объявляем переменную soundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_selection_menu)
        supportActionBar?.hide() // скрыть заголовок
        val squats = findViewById<ImageButton>(R.id.squats_button)
        val armWaving = findViewById<ImageButton>(R.id.arm_waving_button)
        val cancelButton = findViewById<ImageButton>(R.id.imageButton_cancel1)
        val createWorkoutButton = findViewById<Button>(R.id.create_training_button)
        soundManager = SoundManager
        SoundManager.init(this)

        squats.setOnClickListener {
            soundManager.playSound()
            // TODO: Implement the logic for the Start button
            val intent = Intent(this, ExerciseStartSquatsActivity::class.java)
            startActivity(intent)
        }

        armWaving.setOnClickListener {
            soundManager.playSound()
            // TODO: Implement the logic for the Statistics button
        }

        cancelButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(
                this, MainActivity::class.java) // перенаправляет нажатием на MainMenu
            startActivity(intent)
        }
        createWorkoutButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, CreateWorkout::class.java)
            startActivity(intent)
        }

    }
}
