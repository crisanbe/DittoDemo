package com.cvelez.dittodemo.feature.auth.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.auth0.android.provider.WebAuthProvider
import com.cvelez.dittodemo.domain.usecase.AuthenticateUseCase
import com.cvelez.dittodemo.feature.auth.AuthCallback
import com.cvelez.dittodemo.feature.auth.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authCallback: AuthCallback,
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

    private val account = Auth0("DBzarA7CMpmYu0AzEThcIxEbN9CMACSv", "dev-cckak674qqsn6zon.us.auth0.com")
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    fun login(context: Context, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            WebAuthProvider.login(account)
                .withScheme("demo")
                .withScope("openid profile email")
                .withAudience("https://dev-cckak674qqsn6zon.us.auth0.com/api/v2/")
                .withConnection("Username-Password-Authentication")
                .withState("demo")
                .start(context, object : Callback<Credentials, AuthenticationException> {
                    override fun onFailure(exception: AuthenticationException) {
                        Toast.makeText(context, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        _authState.value = AuthState.Error("Login failed: ${exception.message}")
                        onSuccess(false)
                    }

                    override fun onSuccess(credentials: Credentials) {
                        cachedCredentials = credentials
                        authCallback.setCredentials(credentials) // Asegúrate de que esto se llame
                        launch {
                            authenticateUseCase.execute(credentials) // Guarda el token usando el caso de uso
                        }
                        Toast.makeText(
                            context,
                            "Login successful with token: ${credentials.accessToken}",
                            Toast.LENGTH_SHORT
                        ).show()
                        _authState.value = AuthState.LoggedIn(credentials.accessToken)
                        fetchUserProfile()
                        onSuccess(true)
                    }
                })
        }
    }

    private fun fetchUserProfile() {
        cachedCredentials?.let { credentials ->
            val client = AuthenticationAPIClient(account)
            client.userInfo(credentials.accessToken).start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    _authState.value =
                        AuthState.Error("Failed to load profile: ${exception.message}")
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    _authState.value = AuthState.ProfileLoaded(profile)
                }
            })
        }
    }

    fun logout(context: Context) {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(context, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    cachedCredentials = null
                    cachedUserProfile = null
                    _authState.value = AuthState.LoggedOut
                }

                override fun onFailure(exception: AuthenticationException) {
                    _authState.value = AuthState.Error("Logout failed: ${exception.message}")
                }
            })
    }
}
