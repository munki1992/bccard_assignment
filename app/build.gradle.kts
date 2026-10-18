import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
//    id("org.jetbrains.kotlin.plugin.parcelize")                                                   // AGP 내장 Kotlin 에 포함 — 버전 지정 불가
//    alias(libs.plugins.google.services)
//    alias(libs.plugins.firebase.crashlytics)
//    alias(libs.plugins.baselineprofile)
}

// 릴리스 서명키 정보
val keystoreProperties = Properties().apply {
    providers.fileContents(rootProject.layout.projectDirectory.file("keystore/keystore.properties")).asText.orNull?.let { load(it.reader()) }
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

    signingConfigs {
        if (!keystoreProperties.isEmpty) {
            create("release") {
                storeFile     = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias      = keystoreProperties.getProperty("keyAlias")
                keyPassword   = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            signingConfigs.findByName("release")?.let { signingConfig = it }

            optimization {
                enable           = true   // R8 작동

                // isMinifyEnabled   = true  // 코드 축소·최적화·난독화 (구방식)
                // isShrinkResources = true  // 리소스 축소 (구방식)

                // R8 적용 범위 제한 (해제함)
                // packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
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

    testOptions {
        unitTests.isIncludeAndroidResources = true
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
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.bundles.app.test.dependencies)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.bundles.app.android.test.dependencies)
}