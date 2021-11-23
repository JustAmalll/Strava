package com.skillbox.core_db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skillbox.core_db.room.SkillboxDatabase.Companion.DATABASE_VERSION
import com.skillbox.core_db.room.converters.SexConverters
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.shared_model.room.model.AthleteEntities
import com.skillbox.shared_model.room.model.CreateActivitiesEntity

@Database(
    entities = [CreateActivitiesEntity::class, AthleteEntities::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(
    SexConverters::class
)
abstract class SkillboxDatabase : RoomDatabase() {

    abstract fun getAthleteDao(): AthleteDao

    companion object {
        lateinit var instance: SkillboxDatabase
            private set
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "skillboxStrava_db"
        fun buildDataSource(context: Context): SkillboxDatabase {
            val room = Room.databaseBuilder(context, SkillboxDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
            instance = room
            return room
        }
    }
}