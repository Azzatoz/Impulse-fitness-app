package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TrainingMetaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(trainingMeta: TrainingRecordMeta)

    @Update
    fun update(trainingMeta: TrainingRecordMeta)

    @Query("SELECT * FROM TableTrainingMeta")
    fun getAllTrainingMeta(): List<TrainingRecordMeta>

    @Query("SELECT name_of_training FROM TableTrainingMeta")
    fun getAllTrainingNames(): List<String>
}
