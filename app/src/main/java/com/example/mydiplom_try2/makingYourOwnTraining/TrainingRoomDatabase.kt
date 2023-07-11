package com.example.mydiplom_try2.makingYourOwnTraining

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TrainingRecord::class, TrainingRecordMeta::class], version = 1, exportSchema = false)
abstract class TrainingRoomDatabase : RoomDatabase() {

    abstract fun trainingDao(): TrainingDao
    abstract fun trainingMetaDao(): TrainingMetaDao

    companion object {
        @Volatile
        private var INSTANCE: TrainingRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TrainingRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainingRoomDatabase::class.java,
                    "training_database"
                )
                    .addCallback(TrainingDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TrainingDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    // Выполните здесь необходимые действия при создании базы данных
                }
            }
        }
    }
}
