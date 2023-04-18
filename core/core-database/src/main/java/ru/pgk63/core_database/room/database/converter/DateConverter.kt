package ru.pgk63.core_database.room.database.converter

import androidx.room.TypeConverter
import java.util.*

internal class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?): Date?  = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

}