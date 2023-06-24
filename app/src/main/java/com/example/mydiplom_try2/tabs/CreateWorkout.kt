package com.example.mydiplom_try2.tabs
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.makingYourOwnTraining.MainRecordOfTraining

class CreateWorkout : AppCompatActivity() {

    private lateinit var trainingName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)
        supportActionBar?.hide()
        // Убрать нижнюю полоску навигационной панели
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        trainingName = findViewById(R.id.create_name_training) //EditText
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            val tableName = trainingName.text.toString()
            if (tableName.isBlank()) {
                Toast.makeText(this, "Имя таблицы не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            // Создаем базу данных с помощью Room.databaseBuilder()
//            val db = Room.databaseBuilder(
//                applicationContext,
//                TrainingRoomDatabase::class.java,
//                tableName
//            ).build()
//
//            val table = TrainingRecord(
//                tableName = tableName + "_1"
//            )
//
//            lifecycleScope.launch(Dispatchers.IO) {
//                db.trainingDao().insert(table)
//            }


            val intent = Intent(this, MainRecordOfTraining::class.java)
            intent.putExtra("tableName", tableName)
            startActivity(intent)
        }
    }
}








//            val db = TrainingRoomDatabase.getDatabase(applicationContext, tableName)
//            val dao = db.trainingDao()
//
//            if (dao.getTableNames().contains(tableName)) {
//                Toast.makeText(this, "Имя уже занято", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Если таблица с таким именем еще не существует, то создаем ее
//            db.openHelper.writableDatabase.execSQL(
//                "CREATE TABLE IF NOT EXISTS `$tableName` (" +
//                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                        "`date` INTEGER NOT NULL," +
//                        "`duration` INTEGER NOT NULL," +
//                        "`accelerometerData` TEXT NOT NULL," +
//                        "`gyroscopeData` TEXT NOT NULL," +
//                        "`magnetometerData` TEXT NOT NULL)"
//            )
//
//            val intent = Intent(this, MainRecordOfTraining::class.java).apply {
//                putExtra("table_name", tableName)
//            }
//            startActivity(intent)


