package com.hbb20.androidcountrypicker

import android.app.Application
import timber.log.Timber

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
