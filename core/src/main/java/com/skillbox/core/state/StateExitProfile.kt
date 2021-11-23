package com.skillbox.core.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object StateExitProfile {
    private val stateExit = MutableSharedFlow<Boolean>()
    val actionExit: SharedFlow<Boolean> = stateExit

    fun changeToolbarTitle(modelToolbar: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            stateExit.emit(modelToolbar)
        }
    }
}