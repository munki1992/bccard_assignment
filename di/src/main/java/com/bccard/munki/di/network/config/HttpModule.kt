package com.bccard.munki.di.network.config

import com.bccard.munki.core.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * httpModule
 *
 * @author 정문기
 */
val httpModule = module {
    single<Retrofit> {
        AppRetrofitBuilder(
            baseUrl = BuildConfig.BASE_URL
        ).createRetrofit()
    }
}