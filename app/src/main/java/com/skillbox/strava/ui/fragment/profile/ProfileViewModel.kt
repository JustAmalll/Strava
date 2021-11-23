package com.skillbox.strava.ui.fragment.profile

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.network.Athlete
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AthleteRepository
) : BaseViewModel() {

    val athleteObserver = SingleLiveEvent<Athlete>()
    val reAuthStateObserver = SingleLiveEvent<Boolean>()
    val loadDataObserver = SingleLiveEvent<Boolean>()

    fun getAthlete() {
        loadDataObserver.postValue(true)
        launchIO {
            val athlete = repository.getAthlete(::handleState)
            launch {
                athlete?.let { athleteObserver.postValue(it) }
                loadDataObserver.postValue(false)
            }
        }
    }

    fun changeWeight(weight: Int) {
        launchIO {
            repository.putWeightAthlete(weight, ::handleState)
        }
    }
}