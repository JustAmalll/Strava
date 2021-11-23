package com.skillbox.strava.ui.fragment.addActivities

import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.shared_model.network.ActivityType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActivitiesViewModel @Inject constructor(
    private val repository: AthleteRepository
) : BaseViewModel() {

    fun saveLocal(
        name: String,
        type: ActivityType,
        date: String,
        time: Int,
        description: String?,
        distance: Float
    ) {
        launchIO {
            repository.saveLocalActivities(name, type, date, time, description, distance)
        }
    }
}