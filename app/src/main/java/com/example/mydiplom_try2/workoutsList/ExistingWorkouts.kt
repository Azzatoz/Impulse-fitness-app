package com.example.mydiplom_try2.workoutsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.makingYourOwnTraining.TrainingRecord

class ExistingWorkouts : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var workoutsAdapter: WorkoutsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_existing_workouts, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        workoutsAdapter = WorkoutsAdapter(getExistingWorkouts())
        recyclerView.adapter = workoutsAdapter
        return view
    }

    private fun getExistingWorkouts(): List<TrainingRecord> {
        // Здесь вам нужно получить список существующих тренировок из вашего источника данных
        // Например, из базы данных или из API
        // Замените этот код соответствующей логикой получения тренировок

        // Пример данных
        val workout1 = TrainingRecord(1, "Тренировка 1", System.currentTimeMillis(), 60L, "Описание тренировки 1", listOf(1.0f, 2.0f, 3.0f))
        val workout2 = TrainingRecord(2, "Тренировка 2", System.currentTimeMillis(), 45L, "Описание тренировки 2", listOf(4.0f, 5.0f, 6.0f))
        val workout3 = TrainingRecord(3, "Тренировка 3", System.currentTimeMillis(), 30L, "Описание тренировки 3", listOf(7.0f, 8.0f, 9.0f))

        return listOf(workout1, workout2, workout3)
    }
}
