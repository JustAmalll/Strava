package com.skillbox.shared_model.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skillbox.shared_model.room.contract.ActivitiesContract
import java.io.Serializable

@Entity(
    indices = [Index(value = arrayOf(ActivitiesContract.Column.name), unique = true)],
    tableName = ActivitiesContract.tableName
)
data class CreateActivitiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = ActivitiesContract.Column.name)
    val name: String,
    @ColumnInfo(name = ActivitiesContract.Column.type)
    val type: String,
    @ColumnInfo(name = ActivitiesContract.Column.start_date)
    val start_date: String,
    @ColumnInfo(name = ActivitiesContract.Column.elapsed_time)
    val elapsed_time: Int,
    @ColumnInfo(name = ActivitiesContract.Column.description)
    val description: String? = "",
    @ColumnInfo(name = ActivitiesContract.Column.distance)
    val distance: Float? = 0F,
    @ColumnInfo(name = ActivitiesContract.Column.total_elevation_gain)
    val total_elevation_gain: Int? = 0
) : Serializable
