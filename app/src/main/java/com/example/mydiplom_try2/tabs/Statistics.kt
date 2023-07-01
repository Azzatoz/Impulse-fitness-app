package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager

class Statistics : Fragment() {

    private lateinit var soundManager: SoundManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        soundManager = SoundManager
        SoundManager.init(requireContext())

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(requireContext(), Menu::class.java)
            startActivity(intent)
        }

        val goToSquatsButton = view.findViewById<Button>(R.id.to_your_squats)
        goToSquatsButton.setOnClickListener {
            soundManager.playSound()
            val intent = Intent(requireContext(), StatisticsOfSquats::class.java)
            startActivity(intent)
        }

        // TODO: Добавить кнопку "обновить"

        return view
    }
}
