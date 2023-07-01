package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "TableTrainingRecord")
@TypeConverters(FloatListTypeConverter::class)

data class TrainingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name_of_training: String,
    val date: Long,
    val duration: Long,
    val gameRotationVectorData: List<Float>
)