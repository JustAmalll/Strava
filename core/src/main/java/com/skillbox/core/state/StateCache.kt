package com.skillbox.core.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object StateCache {
    private val stateClearCache = MutableSharedFlow<Boolean>()
    val isClearCache: SharedFlow<Boolean> = stateClearCache

    fun changeToolbarTitle(isClear: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            stateClearCache.emit(isClear)
        }
    }
}