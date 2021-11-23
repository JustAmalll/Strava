package com.skillbox.core_network.repository

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.utils.BaseRepository
import com.skillbox.core_network.utils.ErrorHandler
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.map.mapToAthlete
import com.skillbox.shared_model.map.mapToAthleteEntities
import com.skillbox.shared_model.map.mapToСreateActivities
import com.skillbox.shared_model.map.mapToСreateActivitiesEntity
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.shared_model.room.model.CreateActivitiesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AthleteRepositoryImpl @Inject constructor(
    errorHandler: ErrorHandler,
    private val apiAthlete: AthleteApi,
    private val pref: Pref,
    private val athleteDao: AthleteDao
) : BaseRepository(errorHandler = errorHandler), AthleteRepository {

    override suspend fun getAthlete(onState: (State) -> Unit): Athlete? =
        execute(onState = onState, func = {
            val response = apiAthlete.getAthlete().execute()
            if (response.isSuccessful) {
                val resultModel = response.body() as Athlete
                pref.nameProfile = "${resultModel.lastname} ${resultModel.firstname}"
                pref.photoprofile = resultModel.profile ?: ""
                resultModel.weight = athleteDao.getAthlete()?.weight ?: resultModel.weight
                resultModel
            } else
                null
        }, funcLocal = {
            athleteDao.getAthlete()?.mapToAthlete()
        }, funcOther = { resultModel ->
            resultModel?.let {
                athleteDao.insertAthlete(resultModel.mapToAthleteEntities())
            }
            null
        })

    override suspend fun getListAthlete(onState: (State) -> Unit): List<СreateActivity>? =
        execute(onState = onState, func = {
            val response = apiAthlete.getActivities().execute()
            if (response.isSuccessful) {
                val result = response.body() as List<СreateActivity>
                result
            } else
                emptyList()
        }, funcLocal = {
            athleteDao.getAthleteActivities().map { it.mapToСreateActivities() }
        }, funcOther = { resultModel ->
            athleteDao.deleteAthleteActivities()
            if (resultModel.isNotEmpty()) {
                athleteDao.insertAthleteActivities(resultModel.map { it.mapToСreateActivitiesEntity() }
                    .toList())
            }
            emptyList()
        })

    override suspend fun postActivities(
        name: String,
        type: ActivityType,
        date: String,
        time: Int,
        description: String?,
        distance: Float,
        onState: (State) -> Unit
    ): Boolean? =
        execute(onState = onState, func = {
            apiAthlete.createActivities(name, type.name, date, time, description, distance)
                .execute()
            true
        }, funcLocal = {
            false
        }, funcOther = {
            true
        })

    override suspend fun saveLocalActivities(
        name: String,
        type: ActivityType,
        date: String,
        time: Int,
        description: String?,
        distance: Float
    ) = withContext(Dispatchers.IO) {
        athleteDao.insertAthleteActivities(
            listOf(
                CreateActivitiesEntity(
                    0, name, type.name, date, time, description
                        ?: "", distance
                )
            )
        )
    }

    override suspend fun putWeightAthlete(weight: Int, onState: (State) -> Unit): Boolean? =
        execute(onState = onState, func = {
            athleteDao.getAthlete()?.let { athlete ->
                apply { athlete.weight = weight.toDouble() }
                athleteDao.updateWeight(athlete)
            }
            apiAthlete.putWeightProfile(weight)
            true
        }, funcLocal = {
            false
        }, funcOther = {
            true
        })

    override suspend fun clearProfile() {
        athleteDao.deleteAthleteActivities()
        athleteDao.deleteAthlete()
    }

    override suspend fun getLastAthleteDate(): CreateActivitiesEntity? =
        athleteDao.getAthleteLastDate()
}