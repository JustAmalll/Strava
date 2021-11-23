package com.skillbox.strava.di

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.repository.AuthRepository
import com.skillbox.core_network.repository.AuthRepositoryImpl
import com.skillbox.core_network.utils.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        errorHandler: ErrorHandler,
        authApi: AuthApi,
        pref: Pref
    ): AuthRepository {
        return AuthRepositoryImpl(
            errorHandler = errorHandler,
            api = authApi,
            pref = pref
        )
    }
}