package com.example.mydiplom_try2.tabs

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.SoundManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION")
class ActiveExerciseSquats : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var rotationVectorSensor: Sensor

    private lateinit var timerTextView: TextView
    private lateinit var squatsTextView: TextView

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var soundManager: SoundManager
    private var shouldPlaySound = true

    private var lastUpdateTime: Long = 0
    private var lastPitch = 0f
    private var squatsCount = 0
    private var isSquatting = false
    private val squatThreshold: Float = 0.8f

    private lateinit var star1ImageView: ImageView
    private lateinit var star2ImageView: ImageView
    private lateinit var star3ImageView: ImageView
    private lateinit var star4ImageView: ImageView
    private lateinit var star5ImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_exercise_squarts)
        supportActionBar?.hide() // скрыть заголовок
        // Убрать нижнюю полоску навигационной панели
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        star1ImageView = findViewById(R.id.star1)
        star2ImageView = findViewById(R.id.star2)
        star3ImageView = findViewById(R.id.star3)
        star4ImageView = findViewById(R.id.star4)
        star5ImageView = findViewById(R.id.star5)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        timerTextView = findViewById(R.id.timerTextView)
        squatsTextView = findViewById(R.id.scoreTextView)
        soundManager = SoundManager
        SoundManager.init(this)

        val stopButton = findViewById<Button>(R.id.stopButton)
        val minutes = intent.getIntExtra("minutes", 0)
        val seconds = intent.getIntExtra("seconds", 0)

        fun sendInfo() {
            val db = DatabaseHelper(this)
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val squatsCount = squatsTextView.text.toString().toInt()
            db.addStatistic(currentDate, currentTime, squatsCount)
        }

        stopButton.setOnClickListener {
            soundManager.playSound()
            countDownTimer.cancel()
            sensorManager.unregisterListener(this)
            sendInfo()
        }

        countDownTimer = object : CountDownTimer((minutes * 60 + seconds) * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val minutesLeft = millisUntilFinished / 1000 / 60
                val secondsLeft = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutesLeft, secondsLeft)
            }

            override fun onFinish() {
                timerTextView.text = getString(R.string.timer_finished)
                sensorManager.unregisterListener(this@ActiveExerciseSquats)
                sendInfo()
                val intent = Intent(this@ActiveExerciseSquats, StatisticsOfSquats::class.java)
                startActivity(intent)
            }
        }.start()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()

            if (!isSquatting && pitch > squatThreshold && lastPitch <= squatThreshold) {
                // Start of squat detected
                if (shouldPlaySound) {
                    soundManager.playSound()
                }
                squatsCount++
                squatsTextView.text = squatsCount.toString()

                // Обновление картинок звезд
                when (squatsCount) {
                    10 -> {
                        shouldPlaySound = false
                        star1ImageView.setImageResource(R.drawable.star_full)
                        soundManager.playWinSound()
                        shouldPlaySound = true
                    }
                    15 -> {
                        shouldPlaySound = false
                        star2ImageView.setImageResource(R.drawable.star_full)
                        soundManager.playWinSound()
                        shouldPlaySound = true
                    }
                    20 -> {
                        shouldPlaySound = false
                        star3ImageView.setImageResource(R.drawable.star_full)
                        soundManager.playWinSound()
                        shouldPlaySound = true
                    }
                    25 -> {
                        shouldPlaySound = false
                        star4ImageView.setImageResource(R.drawable.star_full)
                        soundManager.playWinSound()
                        shouldPlaySound = true
                    }
                    30 -> {
                        shouldPlaySound = false
                        star5ImageView.setImageResource(R.drawable.star_full)
                        soundManager.playWinSound()
                        shouldPlaySound = true
                    }
                }

                isSquatting = true
            } else if (isSquatting && pitch < -squatThreshold && lastPitch >= -squatThreshold) {
                // End of squat detected
                isSquatting = false
            }

            lastPitch = pitch
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        lastUpdateTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}
