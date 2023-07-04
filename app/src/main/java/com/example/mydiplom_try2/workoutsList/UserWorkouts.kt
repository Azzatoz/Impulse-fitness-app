package com.example.mydiplom_try2.workoutsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.makingYourOwnTraining.TrainingRecord

class UserWorkoutsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var workoutsAdapter: WorkoutsAdapter
    private lateinit var soundManager: SoundManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_workouts, container, false)
        recyclerView = view.findViewById(R.id.userWorkoutsRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получите список пользовательских тренировок из вашего источника данных
        val userWorkouts = getUserWorkouts()

        // Создайте адаптер и передайте список тренировок
        workoutsAdapter = WorkoutsAdapter(userWorkouts)

        // Установите адаптер для RecyclerView
        recyclerView.adapter = workoutsAdapter

        // Установите LayoutManager для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getUserWorkouts(): List<TrainingRecord> {
        // Здесь вы можете получить список пользовательских тренировок из вашего источника данных
        // Например, из базы данных или API

        // Возвращаем заглушечный список для примера
        return emptyList()
    }
}
