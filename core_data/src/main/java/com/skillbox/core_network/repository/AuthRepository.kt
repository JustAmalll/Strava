package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State

interface AuthRepository {
    suspend fun postAuth(
        code: String,
        onState: (State) -> Unit
    ): String?

    suspend fun reauthorize(
        access_token: String,
        onState: (State) -> Unit
    ): String?
}