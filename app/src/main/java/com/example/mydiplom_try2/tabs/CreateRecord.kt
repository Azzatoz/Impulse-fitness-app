package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.creatingRecord.MainRecord
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRecord : Fragment() {

    private lateinit var recordName: EditText // Поле для ввода названия тренировки
    private lateinit var trainingDescription: EditText // Поле для ввода описания тренировки
    private lateinit var soundManager: SoundManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_record, container, false)
        val startButton = view.findViewById<Button>(R.id.startButton)
        trainingDescription = view.findViewById(R.id.create_description_training)
        recordName = view.findViewById(R.id.create_name_training)
        soundManager = SoundManager

        // Сохраняем контекст в переменную context
        val context = requireContext()

        startButton.setOnClickListener {
            val databaseName = recordName.text.toString()
            val description = trainingDescription.text.toString()
            if (databaseName.isBlank()) {
                Toast.makeText(
                    context,
                    "Название не может быть пустым",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val scope = CoroutineScope(Dispatchers.IO) // Создание CoroutineScope для фоновых операций

            scope.launch {
                val database = MyRoomDatabase.getDatabase(
                    context, // Используем сохраненный контекст
                    scope,
                    databaseName
                )

                if (isDatabaseNameExists(database, databaseName)) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            context,
                            "Запись с именем $databaseName уже существует",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }


                val intent = Intent(context, MainRecord::class.java)
                intent.putExtra("databaseName", databaseName) // для записи в таблицу
                intent.putExtra("description", description)

                activity?.runOnUiThread {
                    startActivity(intent)
                }
            }
        }
        return view
    }
    private fun isDatabaseNameExists(database: MyRoomDatabase, databaseName: String): Boolean {
        val existingDatabaseNames = database.metaDao().getAllNames()
        return existingDatabaseNames.any { it == databaseName }
    }


}
