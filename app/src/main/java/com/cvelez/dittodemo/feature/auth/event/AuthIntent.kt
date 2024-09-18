package com.cvelez.dittodemo.feature.auth.event

import com.auth0.android.result.Credentials

sealed class AuthIntent {
    data class Authenticated(val credentials: Credentials) : AuthIntent()
}
