package com.example.mydiplom_try2.creatingRecord

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MetaDao {
    /**
     * Вставляет новую запись в базу данных.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(meta: MetaEntity)
    /**
     * Обновляет существующую запись в базе данных.
     */
    @Update
    fun update(meta: MetaEntity)

    /**
     * Получает имя базы данных (ограничено одной записью).
     */
    @Query("SELECT name_of_record FROM Meta LIMIT 1")
    fun getDatabaseName(): String?

    /**
     * Получает все записи из базы данных.
     */
    @Query("SELECT * FROM Meta")
    fun getAllMeta(): List<MetaEntity>

    /**
     * Получает имена всех тренировок из базы данных.
     */
    @Query("SELECT name_of_record FROM Meta")
    fun getAllNames(): List<String>

    /**
     * Получает запись о тренировке по имени тренировки.
     */
    @Query("SELECT * FROM Meta WHERE name_of_record = :name")
    fun getRecordByName(name: String): MetaEntity?

    /**
     * Удаляет запись о тренировке из базы данных.
     */
    @Delete
    fun delete(training: MetaEntity)
    }