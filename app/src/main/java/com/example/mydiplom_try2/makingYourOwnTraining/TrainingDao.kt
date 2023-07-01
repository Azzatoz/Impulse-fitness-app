package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(training: TrainingRecord)

    @Update
    fun update(training: TrainingRecord)

    @Query("SELECT * FROM TableTrainingRecord")
    fun getAllTrainingRecords(): List<TrainingRecord>

    @Delete
    fun delete(training: TrainingRecord)

    @Query("DELETE FROM TableTrainingRecord")
    fun deleteAll()
}
