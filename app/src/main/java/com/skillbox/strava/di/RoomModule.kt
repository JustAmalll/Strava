package com.skillbox.strava.di

import android.content.Context
import com.skillbox.core_db.room.SkillboxDatabase
import com.skillbox.core_db.room.dao.AthleteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): SkillboxDatabase =
        SkillboxDatabase.buildDataSource(context)

    @Provides
    fun provideGameCardDao(skillboxDatabase: SkillboxDatabase): AthleteDao =
        skillboxDatabase.getAthleteDao()
}