package com.bigmiracle.bottomnavigation

import androidx.room.TypeConverter
import java.text.SimpleDateFormat

class Converters {

    @TypeConverter
    fun fromTimestamp(timeStamp: Long?): String? {
        return timeStamp?.let { FORMATTER.format(timeStamp) }
    }

    @TypeConverter
    fun dateToTimestamp(timeStamp: String?): Long? {
        return timeStamp?.let { FORMATTER.parse(it)?.time }
    }

    companion object{

        val FORMATTER = SimpleDateFormat("yyyy/MM/dd")
    }
}