package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.shared_model.room.model.CreateActivitiesEntity

interface AthleteRepository {
    suspend fun getAthlete(onState: (State) -> Unit): Athlete?

    suspend fun getListAthlete(onState: (State) -> Unit): List<СreateActivity>?

    suspend fun postActivities(
        name: String,
        type: ActivityType,
        date: String,
        time: Int,
        description: String?,
        distance: Float,
        onState: (State) -> Unit
    ): Boolean?

    suspend fun saveLocalActivities(
        name: String,
        type: ActivityType,
        date: String,
        time: Int,
        description: String?,
        distance: Float
    )

    suspend fun putWeightAthlete(weight: Int, onState: (State) -> Unit): Boolean?

    suspend fun clearProfile()

    suspend fun getLastAthleteDate(): CreateActivitiesEntity?
}