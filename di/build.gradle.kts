plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.bccard.munki.di"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    listOf(":data", ":domain", ":core").forEach { implementation(project(it)) }

    implementation(libs.bundles.di.dependencies)
}