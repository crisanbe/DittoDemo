package com.cvelez.dittodemo.auth

import android.util.Log
import com.auth0.android.result.Credentials
import live.ditto.DittoAuthenticationCallback
import live.ditto.DittoAuthenticator

class AuthCallback : DittoAuthenticationCallback {
    private var cachedCredentials: Credentials? = null

    override fun authenticationRequired(authenticator: DittoAuthenticator) {
        cachedCredentials?.let {
            Log.d("AuthCallback", "Attempting to login with access token: ${it.accessToken}")
            authenticator.login(it.accessToken, "dev-cckak674qqsn6zon.us.auth0.com") { _, err ->
                if (err != null) {
                    Log.e("AuthCallback", "Login request failed: ${err.message}")
                } else {
                    Log.d("AuthCallback", "Login request succeeded")
                }
            }
        } ?: Log.e("AuthCallback", "No credentials available, authentication required")
    }

    override fun authenticationExpiringSoon(authenticator: DittoAuthenticator, secondsRemaining: Long) {
        cachedCredentials?.let {
            Log.d("AuthCallback", "Attempting to renew token with access token: ${it.accessToken}")
            authenticator.login(it.accessToken, "dev-cckak674qqsn6zon.us.auth0.com") { _, err ->
                if (err != null) {
                    Log.e("AuthCallback", "Renew token request failed: ${err.message}")
                } else {
                    Log.d("AuthCallback", "Token renewed successfully")
                }
            }
        } ?: Log.e("AuthCallback", "No credentials available to renew token")
    }

    fun setCredentials(credentials: Credentials) {
        cachedCredentials = credentials
        Log.d("AuthCallback", "Credentials set for future authentication. Expires at: ${credentials.expiresAt}")
    }
}