package com.cvelez.dittodemo.feature.auth.state

import com.auth0.android.result.UserProfile

sealed class AuthState {
    object LoggedOut : AuthState()
    data class LoggedIn(val token: String) : AuthState()
    data class ProfileLoaded(val profile: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
}
