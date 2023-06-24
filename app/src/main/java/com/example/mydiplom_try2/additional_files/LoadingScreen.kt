package com.example.mydiplom_try2.additional_files

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.makingYourOwnTraining.MainRecordOfTraining

class LoadingScreen : Activity() {
    private var progressBar: ProgressBar? = null
    private var textView: TextView? = null
    private var thread: Thread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)
        progressBar = findViewById(R.id.progress_bar)
        textView = findViewById(R.id.text_view)
        thread = Thread {
            try {
                // Здесь мы имитируем задержку для демонстрации экрана загрузки
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val intent = Intent(this@LoadingScreen, MainRecordOfTraining::class.java)
            startActivity(intent)
            finish()
        }
        thread!!.start()
    }
}