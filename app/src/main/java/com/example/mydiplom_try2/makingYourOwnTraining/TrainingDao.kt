package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.*

@Dao
interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(training: TrainingRecord)

    @Update
    fun update(training: TrainingRecord)

    @Query("SELECT * FROM training_record")
    fun getAll(): List<TrainingRecord>

    @Query("SELECT * FROM training_record WHERE tableName = :tableName")
    fun getByTableName(tableName: String): List<TrainingRecord>

    // Дополнительные методы, которые вы хотите добавить
    // ...

    @Delete
    fun delete(training: TrainingRecord)

    @Query("DELETE FROM training_record")
    fun deleteAll()
}
