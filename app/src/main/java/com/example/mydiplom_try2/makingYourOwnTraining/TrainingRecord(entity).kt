package com.example.mydiplom_try2.makingYourOwnTraining

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "first_table")

data class TrainingRecord(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val tableName: String,

    @ColumnInfo(name = "date")
    var date: Long = 0,

    @ColumnInfo(name = "duration")
    var duration: Long = 0,

    @ColumnInfo(name = "accelerometer_data")
    var accelerometerData: List<Float> = listOf(),

    @ColumnInfo(name = "gyroscope_data")
    var gyroscopeData: List<Float> = listOf(),

    @ColumnInfo(name = "magnetometer_data")
    var magnetometerData: List<Float> = listOf(),
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
