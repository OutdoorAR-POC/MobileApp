package com.migalska.imageviewer

import androidx.room.TypeConverter

public class Converters {

    @TypeConverter
    fun fromDoubleArray(list: DoubleArray?): String {
        return list?.joinToString(separator = ";") { it.toString()} ?: ""
    }

    @TypeConverter
    fun toDoubleArray(string: String?): DoubleArray {
        return string?.split(";")?.mapNotNull { it.toDoubleOrNull() }?.toDoubleArray()
            ?: doubleArrayOf()
    }

    @TypeConverter
    fun fromArrayListOfFloats(list: FloatArray?): String {
        return list?.joinToString(separator = ";") { it.toString() } ?: ""
    }

    @TypeConverter
    fun toFloatArray(string: String?): FloatArray {
        return string?.split(";")?.mapNotNull { it.toFloatOrNull() }?.toFloatArray()
            ?: floatArrayOf()
    }
}