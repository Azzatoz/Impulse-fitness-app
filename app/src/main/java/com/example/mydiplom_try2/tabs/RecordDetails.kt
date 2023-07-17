package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R

class RecordDetails : Fragment() {

    private lateinit var recordNameTextView: TextView
    private lateinit var recordDurationTextView: TextView
    private lateinit var recordDescriptionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record_details, container, false)
        recordNameTextView = view.findViewById(R.id.training_name_text_view)
        recordDurationTextView = view.findViewById(R.id.training_duration_text_view)
        recordDescriptionTextView = view.findViewById(R.id.training_description_text_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем данные о тренировке из аргументов фрагмента
        val recordName = arguments?.getString(KEY_RECORD_NAME)
        val recordDuration = arguments?.getLong(KEY_TRAINING_DURATION)
        val recordDescription = arguments?.getString(KEY_TRAINING_DESCRIPTION)

        // Устанавливаем значения полей TextView в соответствии с данными о тренировке
        recordName?.let { recordNameTextView.text = it }
        recordDuration?.let { recordDurationTextView.text = it.toString() }
        recordDescription?.let {
            if (it.isBlank()) {
                recordDescriptionTextView.text = getString(R.string.default_description)
            } else {
                recordDescriptionTextView.text = it
            }
        }
    }

    companion object {
        private const val KEY_RECORD_NAME = "recordName"
        private const val KEY_TRAINING_DURATION = "trainingDuration"
        private const val KEY_TRAINING_DESCRIPTION = "trainingDescription"

        fun newInstance(recordName: String, recordDuration: Long, recordDescription: String?): RecordDetails {
            val fragment = RecordDetails()
            val args = Bundle().apply {
                putString(KEY_RECORD_NAME, recordName)
                putLong(KEY_TRAINING_DURATION, recordDuration)
                putString(KEY_TRAINING_DESCRIPTION, recordDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
