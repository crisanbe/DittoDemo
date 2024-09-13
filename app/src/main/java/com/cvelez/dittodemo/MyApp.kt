package com.cvelez.dittodemo

import android.app.Application
import android.util.Log
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies

class MyApp : Application() {
    // Those values should be pasted in 'gradle.properties'. See the notion page for more details.
    private val APP_ID = BuildConfig.APP_ID
    private val ONLINE_AUTH_TOKEN = BuildConfig.ONLINE_AUTH_TOKEN
    lateinit var ditto: Ditto

    override fun onCreate() {
        super.onCreate()

        try {
            val androidDependencies = DefaultAndroidDittoDependencies(this)
            val identity = DittoIdentity.OnlinePlayground(
                androidDependencies,
                appId = APP_ID,
                token = ONLINE_AUTH_TOKEN
            )

            ditto = Ditto(androidDependencies, identity)
            ditto.startSync()
        } catch (e: DittoError) {
            Log.e("Ditto error", e.message ?: "Unknown error")
        }
    }
}