package com.cvelez.dittodemo

import android.app.Application
import android.util.Log
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies

class MyApp : Application() {
    lateinit var ditto: Ditto

    override fun onCreate() {
        super.onCreate()

        try {
            val androidDependencies = DefaultAndroidDittoDependencies(this)
            val identity = DittoIdentity.OnlinePlayground(
                androidDependencies,
                appId = "f9d3e831-2c7d-4d6a-95e0-b599bef666a0",
                token = "03036e78-bb9c-4b26-a507-a66da6b443ff"
            )

            ditto = Ditto(androidDependencies, identity)
            ditto.startSync()
        } catch (e: DittoError) {
            Log.e("Ditto error", e.message ?: "Unknown error")
        }
    }
}