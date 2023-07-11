package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R

class TrainingDetails : Fragment() {

    private lateinit var trainingNameTextView: TextView
    private lateinit var trainingDurationTextView: TextView
    private lateinit var trainingDescriptionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_training_details, container, false)
        trainingNameTextView = view.findViewById(R.id.training_name_text_view)
        trainingDurationTextView = view.findViewById(R.id.training_duration_text_view)
        trainingDescriptionTextView = view.findViewById(R.id.training_description_text_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем данные о тренировке из аргументов фрагмента
        val trainingName = arguments?.getString(KEY_TRAINING_NAME)
        val trainingDuration = arguments?.getLong(KEY_TRAINING_DURATION)
        val trainingDescription = arguments?.getString(KEY_TRAINING_DESCRIPTION)

        // Устанавливаем значения полей TextView в соответствии с данными о тренировке
        trainingName?.let { trainingNameTextView.text = it }
        trainingDuration?.let { trainingDurationTextView.text = it.toString() }
        trainingDescription?.let {
            if (it.isBlank()) {
                trainingDescriptionTextView.text = getString(R.string.default_description)
            } else {
                trainingDescriptionTextView.text = it
            }
        }
    }

    companion object {
        private const val KEY_TRAINING_NAME = "trainingName"
        private const val KEY_TRAINING_DURATION = "trainingDuration"
        private const val KEY_TRAINING_DESCRIPTION = "trainingDescription"

        fun newInstance(trainingName: String, trainingDuration: Long, trainingDescription: String?): TrainingDetails {
            val fragment = TrainingDetails()
            val args = Bundle().apply {
                putString(KEY_TRAINING_NAME, trainingName)
                putLong(KEY_TRAINING_DURATION, trainingDuration)
                putString(KEY_TRAINING_DESCRIPTION, trainingDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
