package com.skillbox.core_db.room.converters

import androidx.room.TypeConverter
import com.skillbox.shared_model.network.Sex


class SexConverters {
    @TypeConverter
    fun convertTypeToString(status: Sex): String = status.name

    @TypeConverter
    fun convertStringToType(statusString: String): Sex =
        Sex.valueOf(statusString)
}