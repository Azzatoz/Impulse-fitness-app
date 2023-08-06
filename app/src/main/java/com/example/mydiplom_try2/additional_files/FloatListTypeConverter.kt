package com.example.mydiplom_try2.additional_files

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FloatListTypeConverter {
    @TypeConverter
    fun fromFloatList(floatList: List<Float>?): String? {
        if (floatList == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Float>>() {}.type
        return gson.toJson(floatList, type)
    }

    @TypeConverter
    fun toFloatList(floatListString: String?): List<Float>? {
        if (floatListString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Float>>() {}.type
        return gson.fromJson<List<Float>>(floatListString, type)
    }
    @TypeConverter
    fun toFloatArrayConverter(floatList: List<Float>?): FloatArray? {
        return floatList?.toFloatArray()
    }

    private fun List<Float>.toFloatArray(): FloatArray {
        val floatArray = FloatArray(size)
        for (i in indices) {
            floatArray[i] = this[i]
        }
        return floatArray
    }
}
