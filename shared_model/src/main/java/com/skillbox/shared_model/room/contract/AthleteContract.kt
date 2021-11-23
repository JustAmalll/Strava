package com.skillbox.shared_model.room.contract

object AthleteContract {
    const val tableName = "AthleteEntities"

    object Column {
        const val id = "id"
        const val username = "username"
        const val resource_state = "resource_state"
        const val firstname = "firstname"
        const val lastname = "lastname"
        const val city = "city"
        const val sex = "sex"
        const val profile_medium = "profile_medium"
        const val profile = "profile"
        const val friend = "friend"
        const val follower = "follower"
        const val country = "country"
        const val weight = "weight"
    }
}