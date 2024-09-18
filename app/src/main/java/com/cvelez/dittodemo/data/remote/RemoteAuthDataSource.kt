package com.cvelez.dittodemo.data.remote

import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.result.UserProfile

class RemoteAuthDataSource(private val authApi: AuthenticationAPIClient) {
    suspend fun login(username: String, password: String): String {
        val credentials = authApi.login(username, password).execute()
        return credentials.accessToken
    }

    suspend fun getUserProfile(token: String): UserProfile {
        return authApi.userInfo(token).execute()
    }
}
