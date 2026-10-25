plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.bccard.munki.baselineprofile"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        minSdk = libs.versions.minSdkBaselineProfile.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["targetAppId"] = "com.bccard.munki"                     // 대상 앱의 applicationId 를 테스트에 넘긴다 (app 과 동일)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // 측정 · 프로파일 생성 대상 앱
    targetProjectPath = ":app"
}

// 연결된 실기기(또는 에뮬레이터)에서 프로파일 생성 — Android 13 이상은 루팅 없이 가능
baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.bundles.baselineprofile.dependencies)
}