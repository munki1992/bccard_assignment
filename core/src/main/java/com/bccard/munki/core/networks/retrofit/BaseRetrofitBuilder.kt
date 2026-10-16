package com.bccard.munki.core.networks.retrofit

import com.bccard.munki.core.BuildConfig
import com.bccard.munki.core.extensions.moshi
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Base Retrofit Builder
 *
 * @author 정문기
 */
abstract class BaseRetrofitBuilder(
    private val baseUrl: String
) {
    companion object {
        private const val TIMEOUT = 30L
        private const val TAG     = "HTTP_LOG"
    }

    private val okHttpBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }

    val retrofit: Retrofit by lazy {
        val jsonLogger = HttpLoggingInterceptor.Logger { message ->
            try {
                Timber.tag(TAG).e(JSONObject(message).toString(2))
            } catch (e: JSONException) {
                Timber.tag(TAG).e(message)
            }
        }

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(
                HttpLoggingInterceptor(jsonLogger).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun addHeaderInterceptor(interceptor: Interceptor): BaseRetrofitBuilder = apply {
        okHttpBuilder.addInterceptor(interceptor)
    }

    fun addAuthenticator(authenticator: Authenticator): BaseRetrofitBuilder = apply {
        okHttpBuilder.authenticator(authenticator)
    }

    abstract fun createRetrofit(): Retrofit
}