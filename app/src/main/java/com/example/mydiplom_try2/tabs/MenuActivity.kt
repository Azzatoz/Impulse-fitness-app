package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MenuActivity : AppCompatActivity() {

    private lateinit var soundManager: SoundManager

    // Создаем экземпляры фрагментов
    private val recordSelectionMenuFragment = RecordSelectionMenu()
    private val statisticsFragment = Statistics()
    private val settingsFragment = Settings()
    private val profileFragment = Profile()
    private val createRecordFragment = CreateRecord()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        supportActionBar?.hide()

        soundManager = SoundManager
        soundManager.initialize(this)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        // Находим BottomNavigationView в макете
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Устанавливаем слушатель для навигационного меню
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            soundManager.playSound()
            when (item.itemId) {
                R.id.action_trainings -> {
                    // При выборе пункта показываем фрагмент ExerciseSelectionMenu
                    showFragment(recordSelectionMenuFragment)
                    true
                }
                R.id.action_statistics -> {
                    // При выборе пункта показываем фрагмент Statistics
                    showFragment(statisticsFragment)
                    true
                }
                R.id.action_settings -> {
                    // При выборе пункта показываем фрагмент Settings
                    showFragment(settingsFragment)
                    true
                }
                R.id.action_profile -> {
                    // При выборе пункта показываем фрагмент Profile
                    showFragment(profileFragment)
                    true
                }
                R.id.action_create_training -> {
                    // При выборе пункта показываем фрагмент CreateWorkout
                    showFragment(createRecordFragment)
                    true
                }
                else -> false
            }
        }

        // Показываем начальный фрагмент (в данном случае exerciseSelectionMenu)
        showFragment(recordSelectionMenuFragment)
    }

    // Функция для показа выбранного фрагмента
    private fun showFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
