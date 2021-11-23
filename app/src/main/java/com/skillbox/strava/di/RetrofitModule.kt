package com.skillbox.strava.di

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.api.AuthApi
import com.skillbox.strava.BuildConfig
import com.skillbox.strava.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val TIME_OUT_CONNECT = 10L
    private const val TIME_OUT_WRITE = 10L
    private const val TIME_OUT_READ = 10L

    @Singleton
    @Provides
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    fun buildOkHttp(cache: Cache, headerInterceptor: HeaderInterceptor): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        with(okHttpClientBuilder) {
            addInterceptor(headerInterceptor)
            cache(cache)
            connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT_WRITE, TimeUnit.SECONDS)
            readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
            followSslRedirects(false)
            followRedirects(false)
            retryOnConnectionFailure(true)
            trustAllSSLCertification()
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
            okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
        }
        return okHttpClientBuilder.build()
    }

    @SuppressLint("LogNotTimber")
    private fun OkHttpClient.Builder.trustAllSSLCertification(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        try {
            val insecureSocketFactory = SSLContext.getInstance("SSL").apply {
                val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
                init(null, trustAllCerts, SecureRandom())
            }.socketFactory
            sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        } catch (ex: Exception) {
            Log.e("OkHttpClient", "Exception ${ex.message}")
        }
        hostnameVerifier { _, _ -> true }
        return this
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.strava.com")
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideRetrofitAuth(retrofit: Retrofit.Builder): AuthApi {
        return retrofit
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitAthlete(retrofit: Retrofit.Builder): AthleteApi {
        return retrofit
            .build()
            .create(AthleteApi::class.java)
    }
}