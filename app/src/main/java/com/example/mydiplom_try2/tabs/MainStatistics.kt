package com.example.mydiplom_try2.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class MainStatistics : AppCompatActivity() {
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_statistics)
        supportActionBar?.hide() // скрыть заголовок
        // Убрать нижнюю полоску навигационной панели
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        soundManager = SoundManager
        SoundManager.init(this)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Добавляем кнопку перехода на StatisticsOfSquats
        val goToSquatsButton = findViewById<Button>(R.id.to_your_squats)
        goToSquatsButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(this, StatisticsOfSquats::class.java)
            startActivity(intent)
        }
    }
}
