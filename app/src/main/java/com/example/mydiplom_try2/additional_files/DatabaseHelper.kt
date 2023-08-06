package com.example.mydiplom_try2.additional_files

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mydiplom_try2.creatingRecord.MyRoomDatabase


class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "records_statistic.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_STATISTICS = "statistics"
        private const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_COUNT = "Count"
        const val COLUMN_NAME = "name" // Добавлено имя записи
        const val COLUMN_ACCURACY = "accuracy" // Добавлена точность выполнения в процентах
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_STATISTICS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_COUNT INTEGER, " +
                "$COLUMN_ACCURACY INTEGER)"
        db?.execSQL(createTable)
    }

    /** Для удаления записи в меню выбора **/
    fun deleteDatabase(databaseName: String) {
        context.deleteDatabase(databaseName)
    }

    fun deleteAllDatabases() {
        // Удаление всех баз данных, созданных с помощью MyRoomDatabase
        val roomDatabaseNames = context.databaseList().filter { it.startsWith("training_db_") }
        for (databaseName in roomDatabaseNames) {
            MyRoomDatabase.deleteDatabase(context, databaseName)
            context.deleteDatabase(DATABASE_NAME)
        }
        // Удаление базы данных exercise.db
        context.deleteDatabase("exercise.db")
        context.deleteDatabase("training_database")
        // Удаление базы данных records_statistic.db
        context.deleteDatabase("records_statistic.db")
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_STATISTICS"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addStatistic(recordName: String, date: String, time: String, score: Int, accuracy: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, recordName)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
            put(COLUMN_COUNT, score)
            put(COLUMN_ACCURACY, accuracy)
        }
        db.insert(TABLE_STATISTICS, null, values)
        db.close()
    }


    fun getAllStatistics(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_STATISTICS", null)
    }
}