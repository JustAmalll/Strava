package com.skillbox.strava.ui.activity.main

import com.skillbox.core.extensions.getDate
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val pref: Pref,
    private val repositoryAthlete: AthleteRepository
) : BaseViewModel() {

    val reAuthStateObserver = SingleLiveEvent<Boolean>()
    val showNotificationObserver = SingleLiveEvent<Boolean>()

    init {
        checkRunner()
    }

    fun exit() {
        val token = pref.accessToken
        launchIO {
            repository.reauthorize(token, ::handleState)?.let { _ ->
                pref.clearProfile()
                reAuthStateObserver.postValue(true)
            }
        }
    }

    private fun checkRunner() {
        launchIO {
            repositoryAthlete.getLastAthleteDate()?.let { athlete ->
                val dateLocalDB = getDate(athlete.start_date)
                val dateNow = Date(System.currentTimeMillis())
                if (dateLocalDB < dateNow) {
                    val dayPref = pref.checkDay
                    if (dayPref != dateNow.day) {
                        pref.checkDay = dateNow.day
                        //Отправляем уведомление об напоминании
                        showNotificationObserver.postValue(true)
                    }
                }
            }
        }
    }
}