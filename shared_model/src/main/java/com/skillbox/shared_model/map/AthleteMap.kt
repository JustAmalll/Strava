package com.skillbox.shared_model.map

import com.skillbox.shared_model.network.Athlete
import com.skillbox.shared_model.network.Sex
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.shared_model.room.model.AthleteEntities
import com.skillbox.shared_model.room.model.CreateActivitiesEntity

fun СreateActivity.mapToСreateActivitiesEntity() =
    CreateActivitiesEntity(
        id, name, type, start_date, elapsed_time, description, distance
    )

fun CreateActivitiesEntity.mapToСreateActivities() =
    СreateActivity(
        id, name, type, start_date, elapsed_time, description, distance ?: 0F
    )

fun AthleteEntities.mapToAthlete() =
    Athlete(
        id,
        username,
        resource_state,
        firstname,
        lastname,
        city,
        sex,
        profile_medium,
        profile,
        friend,
        follower,
        country,
        weight
    )

fun Athlete.mapToAthleteEntities() =
    AthleteEntities(
        id,
        username,
        resource_state,
        firstname,
        lastname,
        city,
        sex ?: Sex.NOT_SEX,
        profile_medium,
        profile,
        friend,
        follower,
        country,
        weight
    )
