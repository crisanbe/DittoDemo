import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cvelez.dittodemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cvelez.dittodemo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            val credsFile = project.file("secure/debug_creds.properties")
            if (credsFile.exists()) {
                val prop = Properties().apply {
                    load(FileInputStream(credsFile))
                }
                buildConfigField("String", "APP_ID", "\"${prop["APP_ID"]}\"")
                buildConfigField("String", "ONLINE_AUTH_TOKEN", "\"${prop["ONLINE_AUTH_TOKEN"]}\"")
            } else {
                throw FileNotFoundException("debug_creds.properties not found in secure directory.")
            }
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            val credsFile = project.file("secure/release_creds.properties")
            if (credsFile.exists()) {
                val prop = Properties().apply {
                    load(FileInputStream(credsFile))
                }
                buildConfigField("String", "APP_ID", "\"${prop["APP_ID"]}\"")
                buildConfigField("String", "ONLINE_AUTH_TOKEN", "\"${prop["ONLINE_AUTH_TOKEN"]}\"")
            } else {
                throw FileNotFoundException("release_creds.properties not found in secure directory.")
            }
        }
    }

    buildFeatures {
        buildConfig = true // Habilita los campos personalizados de BuildConfig
    }

compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("live.ditto:ditto:4.8.0")
    implementation("com.google.accompanist:accompanist-insets:0.28.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.1")

    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.5")
}