package com.example.mydiplom_try2.creatingRecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meta")
data class MetaEntity(
    @PrimaryKey(autoGenerate = true)
    val trainingId: Int = 0,
    val name_of_record: String,
    val date: String,
    val duration: Long,
    val description: String?
)