package com.example.mydiplom_try2.makingYourOwnTraining

import android.provider.BaseColumns

object TrainingContract {

    /* Inner class that defines the table contents */
    class TrainingRecord : BaseColumns {
        companion object {
            const val TABLE_NAME = "training_record"
            const val COLUMN_NAME_TRAINING_NAME = "training_name"
            const val COLUMN_NAME_DATE = "date"
            const val COLUMN_NAME_DURATION = "duration"
            const val COLUMN_NAME_ACCELEROMETER_DATA = "accelerometer_data"
            const val COLUMN_NAME_GYROSCOPE_DATA = "gyroscope_data"
            const val COLUMN_NAME_MAGNETOMETER_DATA = "magnetometer_data"
        }
    }
}