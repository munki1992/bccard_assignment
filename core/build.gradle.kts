plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.bccard.munki.core"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")
        buildConfigField("String", "VERSION_CODE", "\"${libs.versions.versionCode.get()}\"")

        // isRealServer(toml) 로 서버 결정: true → baseUrlProduction, false → baseUrlDevelopment (gradle.properties)
        val isRealServer = libs.versions.isRealServer.get().toBoolean()
        val baseUrl = providers.gradleProperty(if (isRealServer) "baseUrlProduction" else "baseUrlDevelopment").get()
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.bundles.core.dependencies)
    ksp(libs.moshi.kotlin.codegen)
}