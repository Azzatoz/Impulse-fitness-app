package com.example.mydiplom_try2.makingYourOwnTraining

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TrainingRecord::class], version = 2, exportSchema = false)
@TypeConverters(FloatListTypeConverter::class)
abstract class TrainingRoomDatabase : RoomDatabase() {

    abstract fun trainingDao(): TrainingDao

    companion object {
        @Volatile
        private var INSTANCE: TrainingRoomDatabase? = null

        fun getInMemoryDatabase(context: Context): TrainingRoomDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                TrainingRoomDatabase::class.java
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // создание таблицы
                        db.execSQL("CREATE TABLE IF NOT EXISTS training_table (training_name TEXT PRIMARY KEY, training_sets TEXT)")
                    }
                })
                .build()
        }

//        fun getDatabase(context: Context, tableName: String): TrainingRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    TrainingRoomDatabase::class.java,
//                    tableName // имя базы данных
//                ).addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//
//                        // создание таблицы
//                        db.execSQL("CREATE TABLE IF NOT EXISTS $tableName (training_name TEXT PRIMARY KEY, training_sets TEXT)")
//                    }
//                }).allowMainThreadQueries().build()
//                INSTANCE = instance
//                instance
//            }
//        }
    }

//    override fun clearAllTables() {
//        runInTransaction {
//            trainingDao().deleteAll()
//        }
//    }
}

//        fun getDatabase(context: Context, tableName: String): TrainingRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    TrainingRoomDatabase::class.java,
//                    tableName // имя базы данных
//                ).addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        db.execSQL("CREATE TABLE IF NOT EXISTS $tableName (training_name TEXT PRIMARY KEY, training_sets TEXT)")
//                    }
//                }).allowMainThreadQueries().build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
