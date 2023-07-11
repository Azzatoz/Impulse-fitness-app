package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TableTrainingMeta")
data class TrainingRecordMeta(
    @PrimaryKey(autoGenerate = true)
    val trainingId: Int = 0,
    val name_of_training: String,
    val date: Long,
    val duration: Long,
    val description: String?
)