package com.skillbox.strava.di

import com.skillbox.core_cotentProvider.repository.ContactRepository
import com.skillbox.core_cotentProvider.repository.ContactRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ContactRepositoryModule {

    @Provides
    fun provideContactRepository(): ContactRepository {
        return ContactRepositoryImpl()
    }

}