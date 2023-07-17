package com.example.mydiplom_try2.tabs

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
import androidx.room.RoomDatabase
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.makingYourOwnRecord.MetaDao
import com.example.mydiplom_try2.makingYourOwnRecord.MyRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class RecordSelectionMenu : Fragment() {

    private lateinit var buttonContainer: LinearLayout
    private lateinit var metaDao: MetaDao
    private lateinit var database: MyRoomDatabase
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
        // Инициализируйте trainingMetaDao из базы данных
        metaDao = database.metaDao()
        // Заполняйте кнопками список тренировок
        populateRecordButtons()
        return view
    }
    private fun populateRecordButtons() {
        // Получите список тренировок из базы данных
        lifecycleScope.launch {
            val trainingList = withContext(Dispatchers.IO) {
                metaDao.getAllNames()
            }
            buttonContainer.removeAllViews()
            // Создайте и добавьте кнопки на экран
            for (trainingName in trainingList) {
                val button = Button(requireContext())
                button.text = trainingName
                button.setOnClickListener { view ->
                    showPopupMenu(view, trainingName)
                }
                buttonContainer.addView(button)
            }
        }
    }
    private fun showPopupMenu(view: View, trainingName: String) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_delete, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete -> {
                    showDeleteConfirmationDialog(trainingName)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun showDeleteConfirmationDialog(trainingName: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение удаления")
            .setMessage("Вы уверены, что хотите удалить данную запись?")
            .setPositiveButton("Да") { _, _ ->
                deleteTraining(trainingName)
            }
            .setNegativeButton("Нет", null)
            .create()
        alertDialog.show()
    }
    private fun deleteTraining(RecordName: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val recordMeta = metaDao.getRecordByName(RecordName)
                if (recordMeta != null) {
                    metaDao.delete(recordMeta)
                }
            }
            populateRecordButtons()
        }
    }
}