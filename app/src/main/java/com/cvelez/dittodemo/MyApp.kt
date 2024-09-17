package com.cvelez.dittodemo

import android.app.Application
import android.util.Log
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies
import com.cvelez.dittodemo.auth.AuthCallback
import dagger.hilt.android.HiltAndroidApp
import live.ditto.DittoLogLevel
import live.ditto.DittoLogger
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var ditto: Ditto

    @Inject
    lateinit var authCallback: AuthCallback

    override fun onCreate() {
        super.onCreate()

        try {
            DittoLogger.minimumLogLevel = DittoLogLevel.DEBUG
            ditto.startSync()
        } catch (e: DittoError) {
            Log.e("Ditto error", e.message ?: "Unknown error")
        }
    }
}