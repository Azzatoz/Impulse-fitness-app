package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_record")
data class TrainingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableName: String,
    val date: Long,
    val duration: Long,
    val gameRotationVectorData: List<Float>
)







//data class TrainingRecord(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//    val date: Long,
//    val duration: Long,
//    val accelerometerData: List<Float>,
//    val gyroscopeData: List<Float>,
//    val magnetometerData: List<Float>
//) {
//    constructor(
//        date: Long,
//        duration: Long,
//        accelerometerData: List<Float>,
//        gyroscopeData: List<Float>,
//        magnetometerData: List<Float>
//    ) : this(
//        0,
//        date,
//        duration,
//        accelerometerData,
//        gyroscopeData,
//        magnetometerData
//    )
