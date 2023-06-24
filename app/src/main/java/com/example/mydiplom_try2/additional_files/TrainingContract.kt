package com.example.mydiplom_try2

import android.provider.BaseColumns

object TrainingContract {

    object TrainingEntry : BaseColumns {
        const val TABLE_NAME = "training"
        const val COLUMN_NAME_TRAINING_NAME = "training_name"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_SENSOR_DATA = "sensor_data"
    }
}
