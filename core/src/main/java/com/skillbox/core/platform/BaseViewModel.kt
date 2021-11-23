package com.skillbox.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.core.state.StateCache
import com.skillbox.core.utils.Event
import com.skillbox.core_network.utils.Failure
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val mainState = MutableLiveData<Event<State>>()
    val localState = MutableLiveData<ToastModel>()

    protected fun handleState(state: State) {
        if (state is State.Error) {
            when (state.throwable) {
                Failure.ServerError -> {
                    localState.postValue(
                        ToastModel(
                            "Error: Ascent could not load feed.",
                            isLocal = false,
                            isError = true
                        )
                    )
                }
                Failure.LocalSuccess -> {
                    localState.postValue(
                        ToastModel(
                            "Loaded feed from cache",
                            isLocal = true,
                            isError = false
                        )
                    )
                }
                Failure.CacheError -> {
                    StateCache.changeToolbarTitle(true)
                }
            }
            mainState.value = Event(state)
        } else
            mainState.value = Event(state)
    }

    protected fun launch(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) { func.invoke() }

    protected fun launchIO(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) { func.invoke() }
}
