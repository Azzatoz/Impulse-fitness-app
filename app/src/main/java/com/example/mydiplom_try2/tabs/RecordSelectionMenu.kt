package com.example.mydiplom_try2.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.creatingRecord.MetaDao
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordSelectionMenu : Fragment() {

    private lateinit var buttonContainer: LinearLayout
    private lateinit var metaDao: MetaDao
    private lateinit var database: MyRoomDatabase
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selection_menu, container, false)
        buttonContainer = view.findViewById(R.id.buttonContainer)
        val databaseName = arguments?.getString("databaseName") ?: ""
        // Инициализируйте trainingRoomDatabase
        database = MyRoomDatabase.getDatabase(
            requireContext(),
            lifecycleScope,
            databaseName
        )
        databaseHelper = DatabaseHelper(requireContext())
        // Инициализируйте trainingMetaDao из базы данных
        metaDao = database.metaDao()
        // Заполняйте кнопками список тренировок
        populateRecordButtons()
        return view
    }

    private fun populateRecordButtons() {
        // Получите список тренировок из базы данных
        lifecycleScope.launch {
            val recordList = withContext(Dispatchers.IO) {
                metaDao.getAllNames()
            }
            buttonContainer.removeAllViews()
            // Создайте и добавьте кнопки на экран
            for (recordName in recordList) {
                val button = Button(requireContext())
                button.text = recordName
                button.setOnClickListener {
                    val intent = Intent(requireContext(), RecordDetails::class.java)
                    intent.putExtra("recordName", recordName)
                    startActivity(intent) // Важно добавить эту строку для перехода к RecordDetailsActivity
                }
                button.setOnLongClickListener { view ->
                    showPopupMenu(view, recordName)
                    true // Вернуть true, чтобы обозначить, что обработчик удержания обработал событие
                }
                buttonContainer.addView(button)
            }
        }
    }
    private fun showPopupMenu(view: View, recordName: String) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_delete, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete -> {
                    showDeleteConfirmationDialog(recordName)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showDeleteConfirmationDialog(recordName: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение удаления")
            .setMessage("Вы уверены, что хотите удалить данную запись?")
            .setPositiveButton("Да") { _, _ ->
                deleteTraining(recordName)
            }
            .setNegativeButton("Нет", null)
            .create()
        alertDialog.show()
    }

    private fun deleteTraining(recordName: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val recordMeta = metaDao.getRecordByName(recordName)
                if (recordMeta != null) {
                    metaDao.delete(recordMeta)
                }
            }
            databaseHelper.deleteDatabase(recordName) // Удаление базы данных
            populateRecordButtons()
        }
    }
}