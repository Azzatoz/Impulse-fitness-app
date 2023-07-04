package com.example.mydiplom_try2.additional_files

import android.content.Context
import android.media.MediaPlayer
import com.example.mydiplom_try2.R

object SoundManager {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaWin: MediaPlayer
    private var soundOn = true

    fun initialize(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.click_1)
        mediaWin = MediaPlayer.create(context, R.raw.win)
    }

    fun release() {
        mediaPlayer.release()
        mediaWin.release()
    }

    fun playSound() {
        if (soundOn) {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }
    }

    fun playWinSound() {
        if (soundOn) {
            mediaWin.seekTo(0)
            mediaWin.start()
        }
    }

    fun toggleSound() {
        soundOn = !soundOn
    }

    fun isSoundOn(): Boolean {
        return soundOn
    }
}
