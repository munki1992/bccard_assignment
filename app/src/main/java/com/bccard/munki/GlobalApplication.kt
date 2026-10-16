package com.bccard.munki

import android.app.Application
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
        }
    }
}