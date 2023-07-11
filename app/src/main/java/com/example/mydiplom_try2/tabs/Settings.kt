package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.HeightDatabase
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.makingYourOwnTraining.TrainingRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Settings : Fragment() {

    private lateinit var heightDatabase: HeightDatabase
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var soundManager: SoundManager
    private lateinit var trainingRoomDatabase: TrainingRoomDatabase
    private lateinit var scope: CoroutineScope

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        heightDatabase = HeightDatabase(requireContext())
        soundManager = SoundManager
        scope = CoroutineScope(Dispatchers.IO)
        trainingRoomDatabase = TrainingRoomDatabase.getDatabase(requireContext(), scope)

        val deleteDatabaseButton = view.findViewById<Button>(R.id.DeleteDatabase)

        deleteDatabaseButton.setOnClickListener {
            soundManager.playSound()
            databaseHelper.deleteDatabase(requireContext())
            heightDatabase.deleteDatabase(requireContext())
            scope.launch {
                trainingRoomDatabase.trainingDao().deleteAll()
            }
        }

        return view
    }
}
