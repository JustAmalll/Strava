package com.skillbox.strava.ui.fragment.logOut

import android.annotation.SuppressLint
import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogOutViewMode @Inject constructor(
    private val repositoryAuth: AuthRepository,
    private val repository: AthleteRepository,
    private val pref: Pref
) : BaseViewModel() {

    val reAuthStateObserver = SingleLiveEvent<Boolean>()

    @SuppressLint("LogNotTimber")
    fun exit() {
        val token = pref.accessToken
        launchIO {
            repositoryAuth.reauthorize(token, ::handleState)?.let { token ->
                pref.clearProfile()
                launchIO {
                    repository.clearProfile()
                }
                Log.d("LogOutViewMode", "Токен и данные прифиля были успешно очишены")
                reAuthStateObserver.postValue(true)
            }
        }
    }
}