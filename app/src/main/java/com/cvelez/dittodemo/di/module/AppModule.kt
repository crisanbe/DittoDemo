package com.cvelez.dittodemo.di.module

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.cvelez.dittodemo.feature.auth.AuthCallback
import com.cvelez.dittodemo.data.repository.AuthRepositoryImpl
import com.cvelez.dittodemo.domain.repository.AuthRepository
import com.cvelez.dittodemo.domain.usecase.AuthenticateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import live.ditto.Ditto
import live.ditto.DittoError
import live.ditto.DittoIdentity
import live.ditto.android.DefaultAndroidDittoDependencies
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideAuthCallback(): AuthCallback {
        return AuthCallback()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAuthenticateUseCase(authRepository: AuthRepository): AuthenticateUseCase {
        return AuthenticateUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideDitto(context: Context, authCallback: AuthCallback): Ditto {
        val androidDependencies = DefaultAndroidDittoDependencies(context)
        return Ditto(
            androidDependencies,
            DittoIdentity.OnlineWithAuthentication(
                androidDependencies,
                "2d0eae21-6951-422a-84d6-fc4959e90f8c",
                authCallback
            )
        ).apply {
            try {
                startSync()
            } catch (e: DittoError) {
                // Handle error
                Toast.makeText(context, "Ditto error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}
