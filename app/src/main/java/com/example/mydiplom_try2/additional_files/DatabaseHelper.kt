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
        private const val DATABASE_NAME = "exercise.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_STATISTICS = "statistics"
        private const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_COUNT = "Count"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_STATISTICS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_COUNT INTEGER)"
        db?.execSQL(createTable)
    }

    fun deleteDatabase(databaseName: String) {
        context.deleteDatabase(databaseName)
    }

    fun deleteAllDatabases() {
        // Удаление всех баз данных, созданных с помощью MyRoomDatabase
        val roomDatabaseNames = context.databaseList().filter { it.startsWith("training_db_") }
        for (databaseName in roomDatabaseNames) {
            MyRoomDatabase.deleteDatabase(context, databaseName)
        }

        // Удаление базы данных
        context.deleteDatabase(DATABASE_NAME)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_STATISTICS"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addStatistic(date: String, time: String, count: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
            put(COLUMN_COUNT, count)
        }
        db.insert(TABLE_STATISTICS, null, values)
        db.close()
    }


    fun getAllStatistics(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_STATISTICS", null)
    }
}