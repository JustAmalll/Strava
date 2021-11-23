package com.skillbox.core_network.api

import com.skillbox.shared_model.network.OAuthModel
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("oauth/token")
    fun postAuth(
        @Query("client_id") client_id: Int,
        @Query("client_secret") client_secret: String,
        @Query("code") code: String,
        @Query("grant_type") grant_type: String
    ): Call<OAuthModel>

    @POST("oauth/deauthorize")
    fun reauthorization(
        @Query("access_token") access_token: String
    ): Call<OAuthModel>
}