package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.makingYourOwnTraining.TrainingMetaDao
import com.example.mydiplom_try2.makingYourOwnTraining.TrainingRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseSelectionMenu : Fragment() {

    private lateinit var trainingRoomDatabase: TrainingRoomDatabase
    private lateinit var buttonContainer: LinearLayout
    private lateinit var trainingMetaDao: TrainingMetaDao
    private lateinit var database: TrainingRoomDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selection_menu, container, false)
        buttonContainer = view.findViewById(R.id.buttonContainer)

        // Инициализируйте trainingRoomDatabase
        trainingRoomDatabase = TrainingRoomDatabase.getDatabase(requireContext(), lifecycleScope)
        // Инициализируйте trainingMetaDao из базы данных
        trainingMetaDao = database.trainingMetaDao()


        // Заполняйте кнопками список тренировок
        populateTrainingButtons()

        return view
    }


    private fun populateTrainingButtons() {
        // Получите список имен тренировок из базы данных
        lifecycleScope.launch {
            val trainingNames = withContext(Dispatchers.IO) {
                trainingMetaDao.getAllTrainingNames()
            }

            // Создайте и добавьте кнопки на экран
            for (trainingName in trainingNames) {
                val button = Button(requireContext())
                button.text = trainingName
                button.setOnClickListener {
                    // Обработка нажатия кнопки
                    // Перенаправьте на полную информацию о тренировке
                    val intent = Intent(requireContext(), TrainingDetails::class.java)
                    intent.putExtra("trainingName", trainingName)
                    startActivity(intent)
                }
                buttonContainer.addView(button)
            }
        }
    }
}
