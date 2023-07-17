package com.example.mydiplom_try2.makingYourOwnRecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meta")
data class MetaEntity(
    @PrimaryKey(autoGenerate = true)
    val trainingId: Int = 0,
    val name_of_record: String,
    val date: Long,
    val duration: Long,
    val description: String?
)