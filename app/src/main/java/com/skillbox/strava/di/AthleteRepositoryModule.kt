package com.skillbox.strava.di

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AthleteRepositoryImpl
import com.skillbox.core_network.utils.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AthleteRepositoryModule {

    @Provides
    fun provideAthleteRepository(
        errorHandler: ErrorHandler,
        api: AthleteApi,
        pref: Pref,
        athleteDao: AthleteDao
    ): AthleteRepository {
        return AthleteRepositoryImpl(
            errorHandler = errorHandler,
            apiAthlete = api,
            pref = pref,
            athleteDao = athleteDao
        )
    }
}