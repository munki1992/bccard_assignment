plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
//    id("org.jetbrains.kotlin.plugin.parcelize")                                                   // AGP 내장 Kotlin 에 포함 — 버전 지정 불가
//    alias(libs.plugins.google.services)
//    alias(libs.plugins.firebase.crashlytics)
//    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.bccard.munki"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        applicationId = "com.bccard.munki"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    listOf(":di", ":domain", ":core").forEach { implementation(project(it)) }

    // App Dependencies
    implementation(libs.bundles.app.dependencies)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}