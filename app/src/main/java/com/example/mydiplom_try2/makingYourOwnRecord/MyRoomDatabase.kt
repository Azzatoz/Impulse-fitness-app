package com.example.mydiplom_try2.makingYourOwnRecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RecordEntity::class, MetaEntity::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao
    abstract fun metaDao(): MetaDao


    companion object {
        @Volatile
        private var INSTANCE: MyRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope, databaseName: String): MyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "training_db_$databaseName"
                )
                    .addCallback(TrainingDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // Метод для удаления базы данных
        fun deleteDatabase(context: Context, databaseName: String) {
            context.deleteDatabase(databaseName)
        }
    }

    private class TrainingDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch(Dispatchers.IO) {
                    // Выполните здесь необходимые действия при создании базы данных
                }
            }
        }
    }
}
