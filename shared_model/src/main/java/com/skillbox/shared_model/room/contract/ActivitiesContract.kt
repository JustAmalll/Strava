package com.skillbox.shared_model.room.contract

object ActivitiesContract {
    const val tableName = "CreateActivitiesEntity"

    object Column {
        const val id = "id"
        const val name = "name"
        const val type = "type"
        const val start_date = "start_date"
        const val elapsed_time = "elapsed_time"
        const val description = "description"
        const val distance = "distance"
        const val total_elevation_gain = "total_elevation_gain"
    }
}