plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.bccard.munki.data"
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

// Room DB 스키마(JSON)를 버전별로 남긴다 — 마이그레이션 작성·검증용
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    listOf(":domain", ":core").forEach { implementation(project(it)) }

    implementation(libs.bundles.data.dependencies)
    ksp(libs.room.compiler)
}