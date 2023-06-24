package com.example.mydiplom_try2.additional_files

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HeightDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "height.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_HEIGHT = "height"
        const val COLUMN_ID = "id"
        const val COLUMN_HEIGHT = "height"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_HEIGHT (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_HEIGHT INTEGER)"
        db?.execSQL(createTable)
    }

    fun deleteDatabase(context: Context) {
        context.deleteDatabase(DATABASE_NAME)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Do nothing, since we don't need to upgrade the database
    }

    fun addHeight(height: Float) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_HEIGHT, height)
        }
        db.insert(TABLE_HEIGHT, null, values)
        db.close()
    }
    // TODO: функцию отправляющая Height
    fun getHeight(): Float? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_HEIGHT, arrayOf(COLUMN_HEIGHT), null, null, null, null, null)
        val height: Float? = if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_HEIGHT)
            if (columnIndex >= 0) {
                cursor.getFloat(columnIndex)
            } else {
                null
            }
        } else {
            null
        }
        cursor.close()
        db.close()
        return height
    }



}
