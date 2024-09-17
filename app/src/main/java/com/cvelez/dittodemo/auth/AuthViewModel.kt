package com.cvelez.dittodemo.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.result.UserProfile
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authCallback: AuthCallback) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

    private val account = Auth0("DBzarA7CMpmYu0AzEThcIxEbN9CMACSv", "dev-cckak674qqsn6zon.us.auth0.com")
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null


    fun login(context: Context, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Verificar la URL del servidor antes de iniciar sesión
            checkServerUrl("https://dev-cckak674qqsn6zon.us.auth0.com/api/v2/")

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
                        Toast.makeText(context, "Login successful with token: ${credentials.accessToken}", Toast.LENGTH_SHORT).show()
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
                    _authState.value = AuthState.Error("Failed to load profile: ${exception.message}")
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

    private fun checkServerUrl(url: String) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val responseCode = connection.responseCode
            Log.d("AuthViewModel", "Server URL check response code: $responseCode")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Failed to connect to server: ${e.message}")
        }
    }
}

sealed class AuthState {
    object LoggedOut : AuthState()
    data class LoggedIn(val token: String) : AuthState()
    data class ProfileLoaded(val profile: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
}