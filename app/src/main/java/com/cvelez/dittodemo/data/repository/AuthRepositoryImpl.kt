package com.cvelez.dittodemo.data.repository

import com.cvelez.dittodemo.domain.model.AuthToken
import com.cvelez.dittodemo.domain.repository.AuthRepository
import com.auth0.android.result.Credentials

class AuthRepositoryImpl : AuthRepository {

    private var token: AuthToken? = null

    override suspend fun saveToken(credentials: Credentials) {
        token = AuthToken(credentials.accessToken)
    }

    override fun getToken(): AuthToken? {
        return token
    }
}
