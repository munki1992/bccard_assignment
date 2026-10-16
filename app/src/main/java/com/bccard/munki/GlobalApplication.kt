package com.bccard.munki

import android.app.Application
import com.bccard.munki.di.local.localModule
import com.bccard.munki.di.network.config.httpModule
import com.bccard.munki.di.network.impl.apiImplModule
import com.bccard.munki.di.network.service.apiServiceModule
import com.bccard.munki.foundation.di.composeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Global Application
 *
 * @author 정문기
 */
class GlobalApplication : Application() {

    companion object {
        lateinit var instance: GlobalApplication private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initKoin()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    // Init ------------------------------------------------------------------------------------- //

    private fun initKoin() {
        startKoin {
            androidContext(instance)
            modules(
                listOf(
                    localModule,
                    httpModule,
                    apiServiceModule,
                    apiImplModule,
                    composeModule,
                )
            )
        }
    }
}