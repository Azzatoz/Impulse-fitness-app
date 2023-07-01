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
import com.example.mydiplom_try2.makingYourOwnTraining.MainRecordOfTraining

class CreateWorkout : Fragment() {

    private lateinit var trainingName: EditText // Поле для ввода названия тренировки

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_workout, container, false)

        trainingName = view.findViewById(R.id.create_name_training) // Находим EditText по его ID в макете
        val startButton = view.findViewById<Button>(R.id.startButton) // Находим кнопку по ее ID в макете

        startButton.setOnClickListener {
            val tableName = trainingName.text.toString() // Получаем введенное имя тренировки
            if (tableName.isBlank()) {
                Toast.makeText(requireContext(), "Имя таблицы не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), MainRecordOfTraining::class.java) // Создаем намерение для запуска активности MainRecordOfTraining
            intent.putExtra("tableName", tableName) // Передаем имя таблицы в намерение
            startActivity(intent) // Запускаем активность MainRecordOfTraining с переданными данными
        }

        return view
    }
}
