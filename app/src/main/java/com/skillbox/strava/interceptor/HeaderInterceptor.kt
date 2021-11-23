package com.skillbox.strava.interceptor

import com.skillbox.core_db.pref.Pref
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor
@Inject
constructor(
    private val pref: Pref
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        when {
            pref.accessToken.isNotBlank() -> {
                val newRequest =
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer " + pref.accessToken)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .build()

                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(chain.request())
    }
}