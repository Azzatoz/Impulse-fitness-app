package com.example.mydiplom_try2.creatingRecord

import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(training: RecordEntity)

    @Update
    fun update(training: RecordEntity)

    @Query("SELECT * FROM Record")
    fun getAllTrainingRecords(): List<RecordEntity>

    @Delete
    fun delete(training: RecordEntity)

    @Query("DELETE FROM Record")
    fun deleteAll()

}