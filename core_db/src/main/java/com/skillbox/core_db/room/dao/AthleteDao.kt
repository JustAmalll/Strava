package com.skillbox.core_db.room.dao

import androidx.room.*
import com.skillbox.shared_model.room.contract.ActivitiesContract
import com.skillbox.shared_model.room.contract.AthleteContract
import com.skillbox.shared_model.room.model.AthleteEntities
import com.skillbox.shared_model.room.model.CreateActivitiesEntity

@Dao
interface AthleteDao {
    //List activities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthleteActivities(list: List<CreateActivitiesEntity>)

    @Query("SELECT * FROM ${ActivitiesContract.tableName}")
    suspend fun getAthleteActivities(): List<CreateActivitiesEntity>

    @Query("DELETE FROM ${ActivitiesContract.tableName}")
    suspend fun deleteAthleteActivities()

    @Query("SELECT * FROM ${ActivitiesContract.tableName} ORDER BY ${ActivitiesContract.Column.id} DESC LIMIT 1")
    suspend fun getAthleteLastDate(): CreateActivitiesEntity?

    //Athlete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: AthleteEntities)

    @Query("SELECT * FROM ${AthleteContract.tableName} LIMIT 1")
    suspend fun getAthlete(): AthleteEntities?

    @Update
    suspend fun updateWeight(model: AthleteEntities)

    @Query("DELETE FROM ${AthleteContract.tableName}")
    suspend fun deleteAthlete()
}