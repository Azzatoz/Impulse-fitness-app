package com.example.mydiplom_try2.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom_try2.R
import com.example.mydiplom_try2.additional_files.DatabaseHelper
import com.example.mydiplom_try2.additional_files.HeightDatabase
import com.example.mydiplom_try2.additional_files.SoundManager
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Settings : Fragment() {

    private lateinit var heightDatabase: HeightDatabase
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var soundManager: SoundManager
    private lateinit var myRoomDatabase: MyRoomDatabase
    private lateinit var scope: CoroutineScope

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val databaseName = arguments?.getString("databaseName") ?: ""
        databaseHelper = DatabaseHelper(requireContext())
        heightDatabase = HeightDatabase(requireContext())
        soundManager = SoundManager
        scope = CoroutineScope(Dispatchers.IO)
        myRoomDatabase = MyRoomDatabase.getDatabase(requireContext(), scope, databaseName)

        val deleteDatabaseButton = view.findViewById<Button>(R.id.DeleteDatabase)

        deleteDatabaseButton.setOnClickListener {
            soundManager.playSound()
            lifecycleScope.launch(Dispatchers.IO) {
                databaseHelper.deleteAllDatabases()
                heightDatabase.deleteDatabase(requireContext())
                scope.launch {
                    myRoomDatabase.recordDao().deleteAll()
                }
            }
        }

        return view
    }
    companion object {
        fun newInstance(databaseName: String): Settings {
            val fragment = Settings()
            val args = Bundle()
            args.putString("databaseName", databaseName)
            fragment.arguments = args
            return fragment
        }
    }

}
