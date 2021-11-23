package com.skillbox.strava.di

import android.app.Application
import android.content.Context
import com.skillbox.core_db.pref.Pref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
        app: Application
    ): Pref = Pref(context, app)
}