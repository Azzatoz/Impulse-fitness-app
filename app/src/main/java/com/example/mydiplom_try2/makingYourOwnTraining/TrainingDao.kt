package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.*

@Dao
interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(training: TrainingRecord)

    @Update
    fun update(training: TrainingRecord)

//    @Query("DELETE FROM my database")
//    fun deleteAll()

}

