package com.cvelez.dittodemo.domain.usecase

import com.cvelez.dittodemo.domain.repository.AuthRepository
import com.auth0.android.result.Credentials

class AuthenticateUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(credentials: Credentials) {
        authRepository.saveToken(credentials)
    }
}
