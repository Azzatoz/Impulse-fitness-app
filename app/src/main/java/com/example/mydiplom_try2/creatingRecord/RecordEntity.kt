package com.example.mydiplom_try2.creatingRecord

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mydiplom_try2.additional_files.FloatListTypeConverter

@Entity(tableName = "Record")
@TypeConverters(FloatListTypeConverter::class)
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameRotationVectorData: List<Float>
)



