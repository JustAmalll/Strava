package com.skillbox.shared_model.network

data class OAuthModel(
    val token_type: String,
    val expires_at: Long,
    val refresh_token: String,
    val access_token: String,
    val athlete: Athlete
)
