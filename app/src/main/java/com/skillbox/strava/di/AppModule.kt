package com.skillbox.strava.di

import com.skillbox.core_network.utils.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandler()
    }
}