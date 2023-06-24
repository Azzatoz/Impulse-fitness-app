package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.HeightDatabase
import com.example.mydiplom_try2.additional_files.SoundManager

class Settings : AppCompatActivity() {

    private lateinit var heightDatabase: HeightDatabase
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var soundManager: SoundManager // объявляем переменную soundManager

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       supportActionBar?.hide() // скрыть заголовок
       // Убрать нижнюю полоску навигационной панели
       window.decorView.apply {
           systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                   View.SYSTEM_UI_FLAG_FULLSCREEN or
                   View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
       }

        setContentView(R.layout.activity_settings)
        databaseHelper = DatabaseHelper(this)
        heightDatabase = HeightDatabase(this)
        soundManager = SoundManager
        SoundManager.init(this)
        val deleteDatabaseButton = findViewById<Button>(R.id.DeleteDatabase)

        deleteDatabaseButton.setOnClickListener {
            soundManager.playSound()
            databaseHelper.deleteDatabase(this)
            heightDatabase.deleteDatabase(this)
//            val db = TrainingRoomDatabase.getDatabase(this, "my_database.db")
//            val trainingDao = db.trainingDao()
//            trainingDao.deleteAll()
        }
    }
}