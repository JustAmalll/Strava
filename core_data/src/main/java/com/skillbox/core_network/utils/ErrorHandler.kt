package com.skillbox.core_network.utils

import retrofit2.HttpException

class ErrorHandler {
    fun proceedException(exception: Throwable, isLocal: Boolean): Failure {
        return when (exception) {
            is HttpException -> {
                return when (exception.response()?.code()) {
                    502 -> Failure.CacheError
                    else -> {
                        if (isLocal) Failure.LocalSuccess
                        else Failure.ServerError
                    }
                }
            }
            else -> {
                if (isLocal) Failure.LocalSuccess
                else Failure.ServerError
            }
        }
    }
}