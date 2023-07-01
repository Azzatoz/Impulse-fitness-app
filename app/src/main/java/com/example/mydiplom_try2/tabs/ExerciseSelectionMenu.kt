package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class ExerciseSelectionMenu: Fragment() {

    private lateinit var soundManager: SoundManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selection_menu, container, false)

        val squatsButton = view.findViewById<ImageButton>(R.id.squats_button)
        val armWavingButton = view.findViewById<ImageButton>(R.id.arm_waving_button)

        soundManager = SoundManager
        SoundManager.init(requireContext())

        squatsButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(requireContext(), ExerciseStartSquatsActivity::class.java)
            startActivity(intent)
        }

        armWavingButton.setOnClickListener {
            soundManager.playSound()
            // Implement the logic for the Statistics button
        }

        return view
    }
}
