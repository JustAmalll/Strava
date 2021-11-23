package com.skillbox.shared_model.network

data class Athlete(
    val id: Long,
    val username: String?,
    val resource_state: Int,
    val firstname: String,
    val lastname: String,
    val city: String?,
    val sex: Sex?,
    val profile_medium: String?,
    val profile: String?,
    val friend: Int?,
    val follower: Int?,
    val country: String?,
    var weight: Double? = 0.0
)

enum class Sex {
    M, F, NOT_SEX
}
