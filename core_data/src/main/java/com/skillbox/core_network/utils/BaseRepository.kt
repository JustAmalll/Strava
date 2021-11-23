package com.skillbox.core_network.utils

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository(val errorHandler: ErrorHandler) {

    @SuppressLint("LogNotTimber")
    protected suspend inline fun <T> execute(
        crossinline onState: (State) -> Unit,
        noinline func: suspend () -> T,
        noinline funcLocal: suspend () -> T,
        noinline funcOther: suspend (result: T) -> T
    ): T? {
        val result: T
        return try {
            result = withContext(Dispatchers.IO) { func.invoke() }
            withContext(Dispatchers.IO) { result?.let { funcOther.invoke(result) } }
            withContext(Dispatchers.Main) {
                onState(State.Success)
                result
            }
        } catch (e: Exception) {
            Log.e("BaseRepository", e.message.toString())
            withContext(Dispatchers.Main) {
                val result = funcLocal.invoke()
                val isLocal = isSuccessLocal(result)

                val failure = errorHandler.proceedException(e, isLocal)
                onState.invoke(State.Error(failure))
                if (failure != Failure.CacheError) result else null
            }
        }
    }

    fun <T> isSuccessLocal(result: T): Boolean = try {
        (result as List<*>).isNotEmpty()
    } catch (ex: Exception) {
        (result) != null
    }
}