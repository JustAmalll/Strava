package com.skillbox.strava.ui.fragment.auth

import android.net.Uri
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core_network.ConstAPI
import com.skillbox.core_network.repository.AthleteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AthleteRepository
) : BaseViewModel() {

    fun getIsAthlete() {
        launchIO {
            repository.getAthlete(::handleState)
        }
    }

    fun authorizationRequest(): AuthorizationRequest {
        val intentUri: Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", ConstAPI.id_client.toString())
            .appendQueryParameter("redirect_uri", "https://www.strava.com/oauth/token")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter(
                "scope", "read,activity:write,activity:read,profile:write,profile:read_all"
            )
            .build()

        val serviceConfig = AuthorizationServiceConfiguration(
            intentUri,
            Uri.parse("https://www.strava.com/oauth/token")
        )

        return AuthorizationRequest.Builder(
            serviceConfig,
            ConstAPI.id_client.toString(),
            ResponseTypeValues.CODE,
            Uri.parse("https://strava/token")
        ).build()
    }
}