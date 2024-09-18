package com.cvelez.dittodemo.domain.repository

import com.cvelez.dittodemo.domain.model.AuthToken
import com.auth0.android.result.Credentials

interface AuthRepository {
    suspend fun saveToken(credentials: Credentials)
    fun getToken(): AuthToken?
}
