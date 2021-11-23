package com.skillbox.core_network.repository

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.ConstAPI
import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    errorHandler: ErrorHandler,
    private val api: AuthApi,
    private val pref: Pref
) : BaseRepository(errorHandler = errorHandler), AuthRepository {

    override suspend fun postAuth(code: String, onState: (State) -> Unit): String? =
        execute(onState = onState, func = {
            val resultOAuth =
                api.postAuth(
                    client_id = ConstAPI.id_client,
                    client_secret = ConstAPI.client_secret,
                    code = code,
                    grant_type = "authorization_code"
                ).execute().body()!!
            pref.accessToken = resultOAuth.access_token
            resultOAuth.access_token
        }, funcLocal = { "" }, funcOther = { "" })

    override suspend fun reauthorize(access_token: String, onState: (State) -> Unit): String? =
        execute(onState = onState, func = {
            val resultOAuth = api.reauthorization(access_token = access_token).execute().body()!!
            resultOAuth.access_token
        }, funcLocal = { "" }, funcOther = { "" })
}