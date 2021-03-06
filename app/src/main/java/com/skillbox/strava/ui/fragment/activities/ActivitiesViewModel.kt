package com.skillbox.strava.ui.fragment.activities

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.network.–°reateActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
    private val repository: AthleteRepository
) : BaseViewModel() {

    val runnerItemsObserver = SingleLiveEvent<List<–°reateActivity>>()
    val loadDataObserver = SingleLiveEvent<Boolean>()

    fun getAthleteActivities() {
        loadDataObserver.postValue(true)
        launchIO {
            repository.getListAthlete(::handleState)?.let { resultList ->
                launch {
                    list(resultList)
                }
            }
        }
    }

    private fun list(list: List<–°reateActivity>) {
        runnerItemsObserver.postValue(list)
        loadDataObserver.postValue(false)
    }
}