package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.makingYourOwnTraining.MainRecordOfTraining

class CreateWorkout : Fragment() {

    private lateinit var trainingName: EditText // Поле для ввода названия тренировки
    private lateinit var trainingDescription: EditText // Поле для ввода описания тренировки
    private lateinit var soundManager: SoundManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_workout, container, false)
        val startButton = view.findViewById<Button>(R.id.startButton)

        trainingDescription = view.findViewById(R.id.create_description_training)
        trainingName = view.findViewById(R.id.create_name_training)

        soundManager = SoundManager

        startButton.setOnClickListener {
            val tableName = trainingName.text.toString() // Получаем введенное имя тренировки
            val description = trainingDescription.text.toString() // Получаем введенное описание тренировки
            if (tableName.isBlank()) {
                Toast.makeText(requireContext(), "Название не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), MainRecordOfTraining::class.java) // Создаем намерение для запуска активности MainRecordOfTraining
            intent.putExtra("tableName", tableName) // Передаем имя таблицы в намерение
            intent.putExtra("description", description) // Передаем описание тренировки в намерение
            startActivity(intent) // Запускаем активность MainRecordOfTraining с переданными данными

        }

        return view
    }
}
