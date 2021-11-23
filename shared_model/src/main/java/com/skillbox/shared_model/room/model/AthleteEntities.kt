package com.skillbox.shared_model.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skillbox.shared_model.network.Sex
import com.skillbox.shared_model.room.contract.AthleteContract
import java.io.Serializable

@Entity(
    indices = [Index(
        value = arrayOf(
            AthleteContract.Column.firstname,
            AthleteContract.Column.lastname
        ), unique = true
    )],
    tableName = AthleteContract.tableName
)
data class AthleteEntities(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = AthleteContract.Column.username)
    val username: String?,
    @ColumnInfo(name = AthleteContract.Column.resource_state)
    val resource_state: Int,
    @ColumnInfo(name = AthleteContract.Column.firstname)
    val firstname: String,
    @ColumnInfo(name = AthleteContract.Column.lastname)
    val lastname: String,
    @ColumnInfo(name = AthleteContract.Column.city)
    val city: String?,
    @ColumnInfo(name = AthleteContract.Column.sex)
    val sex: Sex,
    @ColumnInfo(name = AthleteContract.Column.profile_medium)
    val profile_medium: String?,
    @ColumnInfo(name = AthleteContract.Column.profile)
    val profile: String?,
    @ColumnInfo(name = AthleteContract.Column.friend)
    val friend: Int?,
    @ColumnInfo(name = AthleteContract.Column.follower)
    val follower: Int?,
    @ColumnInfo(name = AthleteContract.Column.country)
    val country: String?,
    @ColumnInfo(name = AthleteContract.Column.weight)
    var weight: Double? = 0.0
) : Serializable
