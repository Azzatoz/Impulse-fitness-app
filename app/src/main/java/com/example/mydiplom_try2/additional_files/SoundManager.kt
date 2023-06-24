package com.example.mydiplom_try2.additional_files

import android.content.Context
import android.media.MediaPlayer
import com.example.mydiplom_try2.R

object SoundManager {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediawin: MediaPlayer
    private var soundOn = true

    fun init(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.click_1)
        mediawin = MediaPlayer.create(context, R.raw.win)
    }

    fun playSound() {
        if (soundOn) {
            mediaPlayer.start()
        }
    }

    fun playWinSound() {
        if (soundOn) {
            mediawin.start()
        }
    }

    fun toggleSound() {
        soundOn = !soundOn
    }

    fun isSoundOn(): Boolean {
        return soundOn
    }
}
