package com.example.mydiplom_try2.creatingRecord

import androidx.room.*

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(training: RecordEntity)

    @Update
    fun update(training: RecordEntity)

    @Query("SELECT * FROM Record")
    fun getAllGameRotationVectorData(): List<RecordEntity>

    @Delete
    fun delete(training: RecordEntity)

    @Query("DELETE FROM Record")
    fun deleteAll()


    @Query("SELECT * FROM Record WHERE id = :recordId")
    fun getGameRotationVectorDataByRecordId(recordId: Int): List<RecordEntity>
}
